package com.genesyslab.machi.controller.sprint;

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

import com.genesyslab.machi.domain.BacklogGrooming;
import com.genesyslab.machi.service.sprint.BacklogGroomingService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/backlog")
public class BacklogGroomingController {

	public static final Logger LOGGER = LoggerFactory.getLogger(BacklogGroomingController.class);

	@Autowired
	BacklogGroomingService backlogGroomingService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/getAllBacklogsForSprint", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<BacklogGrooming>> getAllBacklogsForSprint(
			@RequestParam(value = "sprintId") String sprintId) {
		LOGGER.info("getAllBacklogsForSprint() | Get all the backlogs for sprint: {}", sprintId);
		List<BacklogGrooming> backlogs = backlogGroomingService.getBacklogsForSprint(sprintId);
		return new ResponseEntity<List<BacklogGrooming>>(backlogs, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllBacklogsForProject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<BacklogGrooming>> getAllBacklogsForProject(
			@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("getAllBacklogsForProject() | Get all the backlogs for project: {}", projectId);
		List<BacklogGrooming> backlogs = backlogGroomingService.getBacklogsForProject(projectId);
		return new ResponseEntity<List<BacklogGrooming>>(backlogs, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updateUser(@RequestParam(value = "id") String id,
			@RequestParam(value = "userId") String userId) {
		LOGGER.info("updateUser() | Updating the backlog: {} to user: {}", id, userId);
		backlogGroomingService.updateBacklog(id, userId, null);
		return new ResponseEntity<String>(converterUtil.toJson("Backlog has been updated successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateEta", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updateEta(@RequestParam(value = "id") String id,
			@RequestParam(value = "eta") String eta) {
		LOGGER.info("updateEta() | Updating the backlog: {} to eta: {}", id, eta);
		backlogGroomingService.updateBacklog(id, null, eta);
		return new ResponseEntity<String>(converterUtil.toJson("Backlog has been updated successfully"), HttpStatus.OK);
	}
}