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

import com.genesyslab.machi.domain.SprintRetrospectiveAndPlanning;
import com.genesyslab.machi.service.sprint.SprintRetrospectiveAndPlanningService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/sprintretro")
public class SprintRetrospectiveAndPlanningController {

	public static final Logger LOGGER = LoggerFactory.getLogger(SprintRetrospectiveAndPlanningController.class);

	@Autowired
	SprintRetrospectiveAndPlanningService sprintRetrospectiveAndPlanningService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/getCommentsForSprint", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SprintRetrospectiveAndPlanning>> getCommentsForSprint(
			@RequestParam(value = "sprintName") String sprintName) {
		LOGGER.info("getCommentsForSprint() | Get all the comments for sprint: {}", sprintName);
		List<SprintRetrospectiveAndPlanning> backlogs = sprintRetrospectiveAndPlanningService
				.getCommentsForSprint(sprintName);
		return new ResponseEntity<List<SprintRetrospectiveAndPlanning>>(backlogs, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCommentsForProject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SprintRetrospectiveAndPlanning>> getCommentsForProject(
			@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("getCommentsForProject() | Get all the comments for project: {}", projectId);
		List<SprintRetrospectiveAndPlanning> backlogs = sprintRetrospectiveAndPlanningService
				.getCommentsForProject(projectId);
		return new ResponseEntity<List<SprintRetrospectiveAndPlanning>>(backlogs, HttpStatus.OK);
	}

	@RequestMapping(value = "/addWhatWentWell", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addWhatWentWell(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "projectId") String projectId, @RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "wentWell") String wentWell) {
		LOGGER.info("addWhatWentWell() | Add comments for user: {}", userId);
		sprintRetrospectiveAndPlanningService.addUserComments(userId, projectId, sprintName, wentWell, null, null,
				null);
		return new ResponseEntity<String>(converterUtil.toJson("Comments has been updated successfully"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/addWhatDidNotGoWell", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addWhatDidNotGoWell(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "projectId") String projectId, @RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "didNotGoWell") String didNotGoWell) {
		LOGGER.info("addWhatDidNotGoWell() | Add comments for user: {}", userId);
		sprintRetrospectiveAndPlanningService.addUserComments(userId, projectId, sprintName, null, didNotGoWell, null,
				null);
		return new ResponseEntity<String>(converterUtil.toJson("Comments has been updated successfully"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/addWhatLearned", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addWhatLearned(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "projectId") String projectId, @RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "learned") String learned) {
		LOGGER.info("addWhatLearned() | Add comments for user: {}", userId);
		sprintRetrospectiveAndPlanningService.addUserComments(userId, projectId, sprintName, null, null, learned, null);
		return new ResponseEntity<String>(converterUtil.toJson("Comments has been updated successfully"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/addWhatPuzzles", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addWhatPuzzles(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "projectId") String projectId, @RequestParam(value = "sprintName") String sprintName,
			@RequestParam(value = "puzzle") String puzzle) {
		LOGGER.info("addWhatPuzzles() | Add comments for user: {}", userId);
		sprintRetrospectiveAndPlanningService.addUserComments(userId, projectId, sprintName, null, null, null, puzzle);
		return new ResponseEntity<String>(converterUtil.toJson("Comments has been updated successfully"),
				HttpStatus.OK);
	}
}