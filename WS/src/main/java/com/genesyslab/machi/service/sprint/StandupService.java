package com.genesyslab.machi.service.sprint;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.StandupDAO;
import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.domain.Standup;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.domain.type.StandupEventType;
import com.genesyslab.machi.helper.MachiConstants;
import com.genesyslab.machi.service.ProjectService;
import com.genesyslab.machi.service.UserService;
import com.genesyslab.machi.service.communication.EmailService;
import com.genesyslab.machi.util.ConverterUtil;

@Service
public class StandupService {

	public static final Logger LOGGER = LoggerFactory.getLogger(StandupService.class);

	@Value("${meeting.mail.subject}")
	private String mailSubject;
	@Value("${meeting.mail.body}")
	private String mailBody;
	@Value("${meeting.reminder.mail.subject}")
	private String reminderMailSubject;
	@Value("${meeting.reminder.mail.body}")
	private String reminderMailBody;

	@Autowired
	StandupDAO standupDAO;
	@Autowired
	ProjectService projectService;
	@Autowired
	UserService userService;
	@Autowired
	EmailService emailService;
	@Autowired
	ConverterUtil converterUtil;

	public Standup getStandup(String id) {
		return standupDAO.findById(id).orElse(null);
	}

	public List<Standup> getAllStandupsForProject(String projectId) {
		return standupDAO.findByProjectIdInOrderByMeetingDateDescUserIdAscTypeAsc(projectId);
	}

	public List<Standup> getCurrentStandupsForProject(String projectId) {
		Standup latestStandup = standupDAO.findTopByProjectIdInOrderByMeetingDateDesc(projectId);
		return standupDAO.findByProjectIdAndMeetingDateOrderByUserIdAscTypeDesc(projectId,
				latestStandup.getMeetingDate());
	}

	public List<Standup> getCurrentStandupsForUser(String userId) {
		return standupDAO.findTop3ByUserIdInOrderByMeetingDateDescTypeAsc(userId);
	}

	public List<Standup> getAllStandupsForUser(String userId) {
		return standupDAO.findByUserIdInOrderByMeetingDateDescTypeAsc(userId);
	}

	public void addStandup(StandupEventType type, String projectId, String sprintId, String userId, Date meetingDate,
			String meetingMinutes) {
		LOGGER.info("addStandup() | Adding new standup for project: {} of type: {} for user: {} on date: {}", projectId,
				type.getMessage(), userId, meetingDate);
		Standup standup = new Standup();
		standup.setType(type);
		standup.setProjectId(projectId);
		standup.setSprintId(sprintId);
		standup.setUserId(userId);
		standup.setMeetingDate(meetingDate);
		standup.setMeetingMinutes(meetingMinutes);

		standup = standupDAO.save(standup);
	}

	public void updateStandup(String id, Date meetingDate, String meetingMinutes) {
		LOGGER.info("updateStandup() | Updating standup: {} for date: {}", id, meetingDate);
		Standup standup = getStandup(id);
		if (null != standup) {
			if (StringUtils.isNotBlank(meetingMinutes))
				standup.setMeetingMinutes(meetingMinutes);
			if (null != meetingDate)
				standup.setMeetingDate(meetingDate);

			standupDAO.save(standup);
		}
	}

	public void sendReminderEmail(String userName) {
		LOGGER.info("sendReminderEmail() | Send reminder email to the user: {}", userName);
		reminderMailBody = reminderMailBody.replace(MachiConstants.PROPERTY_USER_NAME, userName);
		emailService.sendMail(reminderMailSubject, reminderMailBody, userName);
	}

	public void sendMeetingMinutesEmail(String projectId) {
		LOGGER.info("sendMeetingMinutesEmail() | Send Meeting Minutes Email for project: {}", projectId);
		Project myProject = projectService.getProjectDetails(projectId);
		String meetingDate = getMeetingDate(projectId);
		emailService.sendMail(getMailSubject(myProject, meetingDate), getMailBody(myProject, meetingDate),
				getToIds(myProject), getCcId(myProject.getProductOwnerId()));
	}

	private String getMeetingDate(String projectId) {
		Standup latestStandup = standupDAO.findTopByProjectIdInOrderByMeetingDateDesc(projectId);
		return converterUtil.toDateString(latestStandup.getMeetingDate());
	}

	private String getMailSubject(Project myProject, String meetingDate) {
		mailSubject = mailSubject.replace(MachiConstants.PROPERTY_PROJECT_NAME, myProject.getName())
				.replace(MachiConstants.PROPERTY_MEETING_DATE, meetingDate);
		return mailSubject;
	}

	private String getMailBody(Project myProject, String meetingDate) {
		mailBody = mailBody.replace(MachiConstants.PROPERTY_PROJECT_NAME, myProject.getName())
				.replace(MachiConstants.PROPERTY_SPRINT_NAME, myProject.getCurrentSprint())
				.replace(MachiConstants.PROPERTY_MEETING_DATE, meetingDate)
				.replace(MachiConstants.PROPERTY_MOM, prepareMeetingMinutes(myProject.getId()));
		return mailBody;
	}

	private String prepareMeetingMinutes(String projectId) {
		List<Standup> standups = getCurrentStandupsForProject(projectId);
		StringBuilder stringBuilder = new StringBuilder();
		String currentUserId = null;
		for (Standup standup : standups) {
			if (!StringUtils.equals(currentUserId, standup.getUserId())) {
				stringBuilder.append(getUserName(standup.getUserId()));
				stringBuilder.append(":");
				stringBuilder.append(MachiConstants.NEW_LINE);
				currentUserId = standup.getUserId();
			}
			stringBuilder.append(standup.getType().getMessage());
			stringBuilder.append(":");
			stringBuilder.append(MachiConstants.NEW_LINE);
			stringBuilder.append(MachiConstants.TAB);
			stringBuilder.append(standup.getMeetingMinutes());
			stringBuilder.append(MachiConstants.NEW_LINE);
			stringBuilder.append(MachiConstants.NEW_LINE);
		}
		return stringBuilder.toString();
	}

	private String getUserName(String userId) {
		User user = userService.getUser(userId);
		if (null != user) {
			return user.getFullName();
		}
		return null;
	}

	private String getToIds(Project myProject) {
		return myProject.getMembers().stream().map(User::getEmail).collect(Collectors.joining(","));
	}

	private String getCcId(String userId) {
		return userService.getUser(userId).getEmail();
	}
}