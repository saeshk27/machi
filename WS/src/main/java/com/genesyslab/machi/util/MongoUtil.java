package com.genesyslab.machi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.genesyslab.machi.domain.BacklogGrooming;
import com.genesyslab.machi.domain.DBHealthCheck;
import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.domain.SprintMetrics;
import com.genesyslab.machi.domain.Standup;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.domain.type.StandupEventType;
import com.genesyslab.machi.dao.BacklogGroomingDAO;
import com.genesyslab.machi.dao.DBHealthCheckDAO;
import com.genesyslab.machi.dao.ProjectDAO;
import com.genesyslab.machi.dao.SprintMetricsDAO;
import com.genesyslab.machi.dao.StandupDAO;
import com.genesyslab.machi.dao.UserDAO;

@Component
public class MongoUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);

	private final static String DB_HEALTHCHECK_KEY = "Default 1";
	private final static String USER_DATA_FILENAME = "db/user.data";
	private final static String PROJECT_DATA_FILENAME = "db/project.data";
	private final static String BACKLOG_DATA_FILENAME = "db/backlog.data";
	private final static String METRICS_DATA_FILENAME = "db/metrics.data";
	private final static String STANDUP_DATA_FILENAME = "db/standup.data";

	@Autowired
	DBHealthCheckDAO dbHealthCheckDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ProjectDAO projectDAO;
	@Autowired
	BacklogGroomingDAO backlogGroomingDAO;
	@Autowired
	SprintMetricsDAO sprintMetricsDAO;
	@Autowired
	StandupDAO standupDAO;
	@Autowired
	ConverterUtil converterUtil;

	public void deleteAll() throws Exception {
		dbHealthCheckDAO.deleteAll();

		deleteDefaultUsers();
		deleteDefaultProjects();
		deleteDefaultBacklogs();
		deleteDefaultMetrics();
		deleteDefaultStandup();
	}

	private void deleteDefaultUsers() {
		List<String> allKeys = getAllKeysFromFile(USER_DATA_FILENAME);
		userDAO.deleteByIdIn(allKeys);
		LOGGER.info("deleteDefaultUsers() | Default users deleted: {}", allKeys.size());
	}

	private void deleteDefaultProjects() {
		List<String> allKeys = getAllKeysFromFile(PROJECT_DATA_FILENAME);
		projectDAO.deleteByIdIn(allKeys);
		LOGGER.info("deleteDefaultProjects() | Default projects deleted: {}", allKeys.size());
	}

	private void deleteDefaultBacklogs() {
		List<String> allKeys = getAllKeysFromFile(BACKLOG_DATA_FILENAME);
		backlogGroomingDAO.deleteByIdIn(allKeys);
		LOGGER.info("deleteDefaultBacklogs() | Default backlogs deleted: {}", allKeys.size());
	}

	private void deleteDefaultMetrics() {
		List<String> allKeys = getAllKeysFromFile(METRICS_DATA_FILENAME);
		sprintMetricsDAO.deleteByIdIn(allKeys);
		LOGGER.info("deleteDefaultMetrics() | Default metrics deleted: {}", allKeys.size());
	}

	private void deleteDefaultStandup() {
		List<String> allKeys = getAllKeysFromFile(STANDUP_DATA_FILENAME);
		standupDAO.deleteByIdIn(allKeys);
		LOGGER.info("deleteDefaultStandup() | Default standup minutes deleted: {}", allKeys.size());
	}

	public void loadDefaults() throws Exception {
		loadHealthCheckDefaults();
		loadDefaultUsers();
		loadDefaultProjects();
		loadDefaultBacklogs();
		loadDefaultMetrics();
		loadDefaultStandup();
	}

	private void loadHealthCheckDefaults() {
		DBHealthCheck dbHealthCheck = new DBHealthCheck();
		dbHealthCheck.setId(DB_HEALTHCHECK_KEY);
		dbHealthCheck.setHealthy(true);
		dbHealthCheckDAO.save(dbHealthCheck);
		LOGGER.info("loadHealthCheckDefaults() | Defaults loaded for the table: DBHealthCheck");
	}

	private void loadDefaultUsers() {
		List<String> allLines = getAllLinesFromFile(USER_DATA_FILENAME);
		for (String currentLine : allLines) {
			String[] userData = currentLine.split(",");
			if (null == userDAO.findById(userData[0]).orElse(null)) {
				User user = new User();
				user.setId(userData[0]);
				user.setFirstName(userData[1]);
				user.setLastName(userData[2]);
				user.setDesignation(userData[3]);
				user.setEmail(userData[4]);
				user.setMobileNumber(userData[5]);
				user.setEmpId(userData[6]);
				user.setLoginId(userData[7]);
				user.setPassword(userData[8]);
				user.setReportingId(userData[9]);
				user.setProjectId(userData[10]);
				user.setCreateDate(new Date());
				user.setLastLoginDate(new Date());
				userDAO.save(user);
				LOGGER.info("loadDefaultUsers() | Default user: {} has been loaded to the DB", user.getFullName());
			}
		}
	}

	private void loadDefaultProjects() {
		List<String> allLines = getAllLinesFromFile(PROJECT_DATA_FILENAME);
		for (String currentLine : allLines) {
			String[] projectData = currentLine.split(",");
			if (null == projectDAO.findById(projectData[0]).orElse(null)) {
				Project project = new Project();
				project.setId(projectData[0]);
				project.setName(projectData[1]);
				project.setProductOwnerId(projectData[2]);
				project.setCurrentSprint(projectData[3]);
				project.setSprintId(projectData[4]);
				project.setSprintStartDate(converterUtil.toDate(projectData[5]));
				project.setSprintEndDate(converterUtil.toDate(projectData[6]));
				project.setBacklogGrooming(converterUtil.toDate(projectData[7]));
				project.setDailyStandUp(converterUtil.toTime(projectData[8]));
				project.setDailyStandupTime(projectData[8]);
				project.setMemberIds(Arrays.asList("1", "2", "3", "4", "5"));
				project.setCreateDate(new Date());
				projectDAO.save(project);
				LOGGER.info("loadDefaultProjects() | Default project: {} has been loaded to the DB", project.getName());
			}
		}
	}

	private void loadDefaultBacklogs() {
		List<String> allLines = getAllLinesFromFile(BACKLOG_DATA_FILENAME);
		for (String currentLine : allLines) {
			String[] backlogData = currentLine.split(",");
			if (null == backlogGroomingDAO.findById(backlogData[0]).orElse(null)) {
				BacklogGrooming backlog = new BacklogGrooming();
				backlog.setId(backlogData[0]);
				backlog.setTicketNo(backlogData[1]);
				backlog.setTicketTitle(backlogData[2]);
				backlog.setTicketDescription(backlogData[3]);
				backlog.setProjectId(backlogData[4]);
				backlogGroomingDAO.save(backlog);
				LOGGER.info("loadDefaultBacklogs() | Default backlog: {} has been loaded to the DB",
						backlog.getTicketNo());
			}
		}
	}

	private void loadDefaultMetrics() {
		List<String> allLines = getAllLinesFromFile(METRICS_DATA_FILENAME);
		for (String currentLine : allLines) {
			String[] metricsData = currentLine.split(",");
			if (null == sprintMetricsDAO.findById(metricsData[0]).orElse(null)) {
				SprintMetrics metrics = new SprintMetrics();
				metrics.setId(metricsData[0]);
				metrics.setProjectId(metricsData[1]);
				metrics.setUserId(metricsData[2]);
				metrics.setSprintName(metricsData[3]);
				metrics.setSprintDuration(Integer.parseInt(metricsData[4]));
				metrics.setPlannedHours(Integer.parseInt(metricsData[5]));
				metrics.setActualHours(Integer.parseInt(metricsData[6]));
				metrics.setPlannedTickets(Integer.parseInt(metricsData[7]));
				metrics.setTotalWeightage(Integer.parseInt(metricsData[7]));
				metrics.setCompletedTickets(Integer.parseInt(metricsData[8]));
				metrics.setWipTickets(Integer.parseInt(metricsData[9]));
				metrics.setOpenTickets(Integer.parseInt(metricsData[10]));
				metrics.setBlockedTickets(Integer.parseInt(metricsData[11]));

				metrics.setCreateDate(new Date());

				if (metrics.getTotalWeightage() > 0) {
					double mark = metrics.getCompletedTickets() + (0.5 * metrics.getWipTickets())
							+ metrics.getBlockedTickets();
					int percentage = (int) (mark * 100 / metrics.getTotalWeightage());

					metrics.setUserScore(mark);
					metrics.setUserScorePercentage(percentage);
				}
				sprintMetricsDAO.save(metrics);
				LOGGER.info(
						"loadDefaultMetrics() | Default metrics for sprint: {} of user: {} has been loaded to the DB",
						metrics.getSprintName(), metrics.getUserId());
			}
		}
	}

	private void loadDefaultStandup() {
		List<String> allLines = getAllLinesFromFile(STANDUP_DATA_FILENAME);
		for (String currentLine : allLines) {
			String[] standupData = currentLine.split(",");
			if (null == standupDAO.findById(standupData[0]).orElse(null)) {
				Standup standup = new Standup();
				standup.setId(standupData[0]);
				standup.setProjectId(standupData[1]);
				standup.setUserId(standupData[2]);
				standup.setSprintId(standupData[3]);
				standup.setType(StandupEventType.valueOf(standupData[4]));
				standup.setMeetingMinutes(standupData[5]);
				standup.setMeetingDate(new Date());
				standupDAO.save(standup);
				LOGGER.info("loadDefaultStandup() | Default standup: {} has been loaded to the DB",
						standup.getMeetingMinutes());
			}
		}
	}

	private List<String> getAllLinesFromFile(String fileName) {
		try {
			Resource resource = new ClassPathResource(fileName);
			return Files.readAllLines(Paths.get(resource.getURI()));
		} catch (IOException ex) {
			LOGGER.warn("getAllLinesFromFile() | IOException caught for file: {} ", fileName, ex);
		}
		return null;
	}

	private List<String> getAllKeysFromFile(String fileName) {
		List<String> allKeys = new ArrayList<>();
		for (String currentLine : getAllLinesFromFile(fileName)) {
			allKeys.add(currentLine.split(",")[0]);
		}
		return allKeys;
	}
}
