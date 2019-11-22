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

import com.genesyslab.machi.domain.SprintMetrics;
import com.genesyslab.machi.service.sprint.SprintMetricsService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/metrics")
public class SprintMetricsController {

	public static final Logger LOGGER = LoggerFactory.getLogger(SprintMetricsController.class);

	@Autowired
	SprintMetricsService sprintMetricsService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/getMetricsForSprint", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SprintMetrics>> getMetricsForSprint(
			@RequestParam(value = "sprintName") String sprintName) {
		LOGGER.info("getMetricsForSprint() | Get the metrics for sprint: {}", sprintName);
		List<SprintMetrics> metrics = sprintMetricsService.getMetricsForSprint(sprintName);
		return new ResponseEntity<List<SprintMetrics>>(metrics, HttpStatus.OK);
	}

	@RequestMapping(value = "/getMetricsForProject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SprintMetrics>> getMetricsForProject(
			@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("getMetricsForProject() | Get the metrics for project: {}", projectId);
		List<SprintMetrics> metrics = sprintMetricsService.getMetricsForProject(projectId);
		return new ResponseEntity<List<SprintMetrics>>(metrics, HttpStatus.OK);
	}

	@RequestMapping(value = "/addMetricsForProject", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addMetricsForProject(@RequestParam(value = "projectId") String projectId,
			@RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "sprintDuration") int sprintDuration) {
		LOGGER.info("addMetricsForProject() | Initiating metrics for project: {} for sprint: {}", projectId,
				sprintName);
		sprintMetricsService.addMetricsForProject(projectId, sprintName, sprintDuration);
		return new ResponseEntity<String>(converterUtil.toJson("Sprint metrics has been successfully initiated"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/addMetricsForUser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addMetricsForUser(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "projectId") String projectId, @RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "sprintDuration") int sprintDuration,
			@RequestParam(value = "plannedHours") int plannedHours,
			@RequestParam(value = "plannedTickets") int plannedTickets) {
		LOGGER.info("addMetricsForUser() | Initiating metrics for user: {} of project: {} for sprint: {}", userId,
				projectId, sprintName);
		sprintMetricsService.addMetricsForUser(userId, projectId, sprintName, sprintDuration, plannedHours,
				plannedTickets);
		return new ResponseEntity<String>(converterUtil.toJson("Sprint metrics has been successfully initiated"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/updateMetrics", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updateMetrics(@RequestParam(value = "id") String id,
			@RequestParam(value = "plannedHours") int plannedHours,
			@RequestParam(value = "plannedTickets") int plannedTickets) {
		LOGGER.info("updateMetrics() | Updating sprint metrics during planning phase for sprint: {}", id);
		sprintMetricsService.updateMetrics(id, plannedHours, plannedTickets);
		return new ResponseEntity<String>(converterUtil.toJson("Sprint metrics has been successfully updated"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/closeSprintMetrics", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> closeSprintMetrics(@RequestParam(value = "id") String id,
			@RequestParam(value = "actualHours") int actualHours,
			@RequestParam(value = "completedTickets") int completedTickets,
			@RequestParam(value = "wipTickets") int wipTickets, @RequestParam(value = "openTickets") int openTickets,
			@RequestParam(value = "blockedTickets") int blockedTickets) {
		LOGGER.info("closeSprintMetrics() | Updating sprint metrics during closing phase for sprint: {}", id);
		sprintMetricsService.updateMetrics(id, actualHours, completedTickets, wipTickets, openTickets, blockedTickets);
		return new ResponseEntity<String>(converterUtil.toJson("Sprint metrics has been successfully updated"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/remindTeamRating", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> remindTeamRating(@RequestParam(value = "sprintName") String sprintName) {
		LOGGER.info("remindTeamRating() | Remind team about their performance for sprint: {}", sprintName);
		sprintMetricsService.remindTeamRating(sprintName);
		return new ResponseEntity<String>(
				converterUtil.toJson("Team has been notified of the sprint metrics successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/getMetricsForUser", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<SprintMetrics> getMetricsForUser(@RequestParam(value = "userId") String userId) {
		LOGGER.info("getMetricsForUser() | Get the metrics for user: {}", userId);
		SprintMetrics metrics = sprintMetricsService.getSprintMetricsForUser(userId);
		return new ResponseEntity<SprintMetrics>(metrics, HttpStatus.OK);
	}
}