package com.genesyslab.machi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.service.ProjectService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/project")
public class ProjectController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	ProjectService projectService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/getProjectDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Project> getProjectDetails(@RequestParam(value = "id") String id) {
		LOGGER.info("getProjectDetails() | Get the details of the project: {}", id);
		Project project = projectService.getProjectDetails(id);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	@RequestMapping(value = "/getOwnerProjects", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Project>> getOwnerProjects(
			@RequestParam(value = "productOwnerId") String productOwnerId) {
		LOGGER.info("getOwnerProjects() | Get the list of the projects: {}", productOwnerId);
		List<Project> projects = projectService.getProjectsForProductOwner(productOwnerId);
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addProject(@RequestParam(value = "name") String name,
			@RequestParam(value = "productOwnerId") String productOwnerId,
			@RequestParam(value = "memberIds") List<String> memberIds,
			@RequestParam(value = "currentSprint") String currentSprint,
			@RequestParam(value = "sprintId") String sprintId,
			@RequestParam(value = "backlogGrooming") String backlogGrooming,
			@RequestParam(value = "dailyStandUp") String dailyStandUp,
			@RequestParam(value = "sprintStartDate") String sprintStartDate,
			@RequestParam(value = "sprintEndDate") String sprintEndDate) {
		LOGGER.info("addProject() | Adding new project of name:{}", name);
		projectService.addProject(name, productOwnerId, memberIds, currentSprint, sprintId,
				converterUtil.toDate(backlogGrooming), converterUtil.toTime(dailyStandUp),
				converterUtil.toDate(sprintStartDate), converterUtil.toDate(sprintEndDate));
		return new ResponseEntity<String>(converterUtil.toJson("Project has been added successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateProject", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updateProject(@RequestParam(value = "id") String id,
			@RequestParam(value = "name") String name, @RequestParam(value = "productOwnerId") String productOwnerId,
			@RequestParam(value = "memberIds") List<String> memberIds,
			@RequestParam(value = "currentSprint") String currentSprint,
			@RequestParam(value = "sprintId") String sprintId,
			@RequestParam(value = "backlogGrooming") String backlogGrooming,
			@RequestParam(value = "dailyStandUp") String dailyStandUp,
			@RequestParam(value = "sprintStartDate") String sprintStartDate,
			@RequestParam(value = "sprintEndDate") String sprintEndDate) {
		LOGGER.info("updateProject() | Updating the project of name:{}", name);
		projectService.updateProject(id, name, productOwnerId, memberIds, currentSprint, sprintId,
				converterUtil.toDate(backlogGrooming), converterUtil.toTime(dailyStandUp),
				converterUtil.toDate(sprintStartDate), converterUtil.toDate(sprintEndDate));
		return new ResponseEntity<String>(converterUtil.toJson("Project has been updated successfully"), HttpStatus.OK);
	}
}