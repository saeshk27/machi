package com.genesyslab.machi.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.ProjectDAO;
import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.service.sprint.StandupService;
import com.genesyslab.machi.util.ConverterUtil;

@Service
public class ProjectService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

	@Autowired
	ProjectDAO projectDAO;
	@Autowired
	UserService userService;
	@Autowired
	StandupService standupService;
	@Autowired
	ConverterUtil converterUtil;

	public Project getProject(String id) {
		return projectDAO.findById(id).orElse(null);
	}

	public List<Project> getProjectsForProductOwner(String productOwnerId) {
		return projectDAO.findByProductOwnerId(productOwnerId);
	}

	public Project getProjectDetails(String id) {
		Project project = getProject(id);
		if (null != project) {
			List<User> members = userService.getUsersForProject(id).stream()
					.peek(u -> u.setMeetingNotes(standupService.getCurrentStandupsForUser(u.getId())))
					.collect(Collectors.toList());
			project.setMembers(members);
			project.setProductOwnerName(userService.getUser(project.getProductOwnerId()).getFullName());
			LOGGER.info("getProjectDetails() | Details project name: {}, team size: {}", project.getName(),
					members.size());
		}
		return project;
	}

	public void addProject(String name, String productOwnerId, List<String> memberIds, String currentSprint,
			String sprintId, Date backlogGrooming, Date dailyStandUp, Date sprintStartDate, Date sprintEndDate) {
		LOGGER.info("addProject() | Adding new project: {}", name);
		Project project = new Project();
		project.setName(name);
		project.setProductOwnerId(productOwnerId);
		project.setMemberIds(memberIds);
		project.setCurrentSprint(currentSprint);
		project.setSprintId(sprintId);
		project.setSprintStartDate(sprintStartDate);
		project.setSprintEndDate(sprintEndDate);
		project.setBacklogGrooming(backlogGrooming);
		project.setDailyStandUp(dailyStandUp);
		project.setDailyStandupTime(getTimeFromDate(dailyStandUp));
		project.setCreateDate(new Date());

		project = projectDAO.save(project);

		userService.updateProjectForUsers(project.getId(), memberIds);
	}

	public void updateProject(String id, String name, String productOwnerId, List<String> memberIds,
			String currentSprint, String sprintId, Date backlogGrooming, Date dailyStandUp, Date sprintStartDate,
			Date sprintEndDate) {
		LOGGER.info("updateProject() | Updating project: {}", id);
		Project project = getProject(id);
		if (null != project) {
			if (StringUtils.isNotBlank(name))
				project.setName(name);
			if (StringUtils.isNotBlank(productOwnerId))
				project.setProductOwnerId(productOwnerId);
			if (StringUtils.isNotBlank(currentSprint))
				project.setCurrentSprint(currentSprint);
			if (StringUtils.isNotBlank(sprintId))
				project.setSprintId(sprintId);
			if (null != memberIds && memberIds.size() > 0)
				project.setMemberIds(memberIds);
			if (null != sprintStartDate)
				project.setSprintStartDate(sprintStartDate);
			if (null != sprintEndDate)
				project.setSprintEndDate(sprintEndDate);
			if (null != backlogGrooming)
				project.setBacklogGrooming(backlogGrooming);
			if (null != dailyStandUp) {
				project.setDailyStandUp(dailyStandUp);
				project.setDailyStandupTime(getTimeFromDate(dailyStandUp));
			}
			project.setLastUpdateDate(new Date());

			projectDAO.save(project);

			userService.updateProjectForUsers(id, memberIds);
		}
	}

	private String getTimeFromDate(Date date) {
		if (null == date) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
	}
}