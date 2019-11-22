package com.genesyslab.machi.service.sprint;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.SprintMetricsDAO;
import com.genesyslab.machi.domain.SprintMetrics;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.helper.MachiConstants;
import com.genesyslab.machi.service.UserService;
import com.genesyslab.machi.service.communication.EmailService;

@Service
public class SprintMetricsService {

	@Value("${sprint.rating.cutoff.min}")
	private int minCutoff;
	@Value("${sprint.rating.cutoff.max}")
	private int maxCutoff;
	@Value("${sprint.rating.mail.min.subject}")
	private String minMailSubject;
	@Value("${sprint.rating.mail.min.body}")
	private String minMailBody;
	@Value("${sprint.rating.mail.max.subject}")
	private String maxMailSubject;
	@Value("${sprint.rating.mail.max.body}")
	private String maxMailBody;

	public static final Logger LOGGER = LoggerFactory.getLogger(SprintMetricsService.class);

	@Autowired
	UserService userService;
	@Autowired
	EmailService emailService;
	@Autowired
	SprintMetricsDAO sprintMetricsDAO;

	public SprintMetrics getSprintMetrics(String id) {
		return sprintMetricsDAO.findById(id).orElse(null);
	}

	public SprintMetrics getSprintMetricsForUser(String userId) {
		List<SprintMetrics> metrics = sprintMetricsDAO.findByUserId(userId);
		for (SprintMetrics metric : metrics) {
			if (metric.getUserScorePercentage() > 0) {
				return metric;
			}
		}
		return null;
	}

	public List<SprintMetrics> getMetricsForSprint(String sprintName) {
		return calculateRanking(sprintMetricsDAO.findBySprintNameOrderByUserId(sprintName));
	}

	public List<SprintMetrics> getMetricsForProject(String projectId) {
		return calculateRanking(sprintMetricsDAO.findByProjectIdOrderBySprintNameAscUserIdAsc(projectId));
	}

	private List<SprintMetrics> calculateRanking(List<SprintMetrics> metrics) {
		AtomicInteger rank = new AtomicInteger();
		return metrics.stream().sorted(Comparator.comparing(SprintMetrics::getSprintName)
				.thenComparingInt(SprintMetrics::getUserScorePercentage).reversed()).peek(s -> {
					s.setRank(rank.incrementAndGet());
					s.setUserName(userService.getUser(s.getUserId()).getFullName());
				}).collect(Collectors.toList());
	}

	public void addMetricsForProject(String projectId, String sprintName, int sprintDuration) {
		LOGGER.info(
				"addMetricsForProject() | Adding new sprint metrics for all users of the project: {} for sprint: {}",
				projectId, sprintName);
		userService.getUsersForProject(projectId)
				.forEach(u -> addMetricsForUser(u.getId(), projectId, sprintName, sprintDuration, 0, 0));
	}

	public void addMetricsForUser(String userId, String projectId, String sprintName, int sprintDuration,
			int plannedHours, int plannedTickets) {
		LOGGER.info("addMetricsForUser() | Adding new sprint metrics to user: {} for sprint: {}", userId, sprintName);
		SprintMetrics sprintMetrics = new SprintMetrics();
		sprintMetrics.setUserId(userId);
		sprintMetrics.setProjectId(projectId);
		sprintMetrics.setSprintName(sprintName);
		sprintMetrics.setSprintDuration(sprintDuration);
		sprintMetrics.setPlannedHours(plannedHours);
		sprintMetrics.setPlannedTickets(plannedTickets);
		sprintMetrics.setTotalWeightage(plannedTickets);// 1 Mark given for each ticket planned
		sprintMetrics.setCreateDate(new Date());
		sprintMetricsDAO.save(sprintMetrics);
	}

	public void updateMetrics(String id, int plannedHours, int plannedTickets) {
		LOGGER.info("updateMetrics() | Updating sprint metrics during planning phase for sprint: {}", id);
		SprintMetrics sprintMetrics = getSprintMetrics(id);
		if (null != sprintMetrics) {
			if (plannedHours != 0)
				sprintMetrics.setPlannedHours(plannedHours);
			if (plannedTickets != 0) {
				sprintMetrics.setPlannedTickets(plannedTickets);
				sprintMetrics.setTotalWeightage(plannedTickets);// 1 Mark given for each ticket planned
			}
			sprintMetricsDAO.save(sprintMetrics);
		}
	}

	public void updateMetrics(String id, int actualHours, int completedTickets, int wipTickets, int openTickets,
			int blockedTickets) {
		LOGGER.info("updateMetrics() | Updating sprint metrics during evaluation for sprint: {}", id);
		SprintMetrics sprintMetrics = getSprintMetrics(id);
		if (null != sprintMetrics) {
			if (actualHours != 0)
				sprintMetrics.setActualHours(actualHours);
			if (completedTickets != 0)
				sprintMetrics.setCompletedTickets(completedTickets);
			if (wipTickets != 0)
				sprintMetrics.setWipTickets(wipTickets);
			if (openTickets != 0)
				sprintMetrics.setOpenTickets(openTickets);
			if (blockedTickets != 0)
				sprintMetrics.setBlockedTickets(blockedTickets);

			calculateRating(sprintMetrics);
		}
	}

	/*
	 * ScorerUtil has to be used after completing the JIRA integration
	 */
	private void calculateRating(SprintMetrics sprintMetrics) {
		LOGGER.info("calculateRating() | Calculate rating for user: {} for sprint: {}", sprintMetrics.getUserId(),
				sprintMetrics.getSprintName());
		double mark = sprintMetrics.getCompletedTickets() + (0.5 * sprintMetrics.getWipTickets())
				+ sprintMetrics.getBlockedTickets();
		int percentage = (int) (mark * 100 / sprintMetrics.getTotalWeightage());

		sprintMetrics.setUserScore(mark);
		sprintMetrics.setUserScorePercentage(percentage);

		LOGGER.info("calculateRating() | Calculated mark: {} and percentage: {} for user: {} for sprint: {}", mark,
				percentage, sprintMetrics.getUserId(), sprintMetrics.getSprintName());

		sprintMetricsDAO.save(sprintMetrics);
	}

	public void remindTeamRating(String sprintName) {
		LOGGER.info("remindTeamRating() | Check for the seamless delivery of the scrum team: {}", sprintName);
		List<SprintMetrics> metrics = getMetricsForSprint(sprintName);
		metrics.forEach(s -> remindUserRating(s));
	}

	private void remindUserRating(SprintMetrics sprintMetrics) {
		if (sprintMetrics.getUserScorePercentage() < minCutoff) {
			User user = userService.getUser(sprintMetrics.getUserId());
			LOGGER.info(
					"remindUserRating() | User: {} has been notified of not matching the expectations for the sprint: {}",
					user.getFullName(), sprintMetrics.getSprintName());
			sendEmail(user, minMailSubject, minMailBody);
		} else if (sprintMetrics.getUserScorePercentage() > maxCutoff) {
			User user = userService.getUser(sprintMetrics.getUserId());
			LOGGER.info("remindUserRating() | User: {} has been notified for over burdend for the sprint: {}",
					user.getFullName(), sprintMetrics.getSprintName());
			sendEmail(user, maxMailSubject, maxMailBody);
		}
	}

	private void sendEmail(User user, String subject, String body) {
		body = body.replace(MachiConstants.PROPERTY_USER_NAME, user.getFullName());
		emailService.sendMail(subject, body, user.getEmail());
	}
}