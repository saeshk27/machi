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

import com.genesyslab.machi.domain.Standup;
import com.genesyslab.machi.domain.type.StandupEventType;
import com.genesyslab.machi.service.sprint.StandupService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/standup")
public class StandupController {

	public static final Logger LOGGER = LoggerFactory.getLogger(StandupController.class);

	@Autowired
	StandupService standupService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/getAllMeetingNotesForUser", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Standup>> getAllMeetingNotesForUser(@RequestParam(value = "userId") String userId) {
		LOGGER.info("getAllMeetingNotesForUser() | Get all the meeting notes of the user: {}", userId);
		List<Standup> standups = standupService.getAllStandupsForUser(userId);
		return new ResponseEntity<List<Standup>>(standups, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCurrentMeetingNotesForUser", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Standup>> getCurrentMeetingNotesForUser(@RequestParam(value = "userId") String userId) {
		LOGGER.info("getCurrentMeetingNotesForUser() | Get all the meeting notes of the user: {}", userId);
		List<Standup> standups = standupService.getCurrentStandupsForUser(userId);
		return new ResponseEntity<List<Standup>>(standups, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllMeetingNotesForProject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Standup>> getAllMeetingNotesForProject(
			@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("getAllMeetingNotesForProject() | Get all the meeting notes of the project: {}", projectId);
		List<Standup> standups = standupService.getAllStandupsForProject(projectId);
		return new ResponseEntity<List<Standup>>(standups, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCurrentMeetingNotesForProject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Standup>> getCurrentMeetingNotesForProject(
			@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("getCurrentMeetingNotesForProject() | Get all the meeting notes of the project: {}", projectId);
		List<Standup> standups = standupService.getCurrentStandupsForProject(projectId);
		return new ResponseEntity<List<Standup>>(standups, HttpStatus.OK);
	}

	@RequestMapping(value = "/addMeetingNotesPast", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addMeetingNotesPast(@RequestParam(value = "projectId") String projectId,
			@RequestParam(value = "sprintId", required = false) String sprintId,
			@RequestParam(value = "userId") String userId, @RequestParam(value = "meetingDate") String meetingDate,
			@RequestParam(value = "meetingMinutes") String meetingMinutes) {
		LOGGER.info("addMeetingNotesPast() | Adding new standup for project: {} for user: {} on date: {}", projectId,
				userId, meetingDate);
		standupService.addStandup(StandupEventType.PAST, projectId, sprintId, userId, converterUtil.toDate(meetingDate),
				meetingMinutes);
		return new ResponseEntity<String>(
				converterUtil.toJson("Meeting notes for past days has been added successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/addMeetingNotesNext", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addMeetingNotesNext(@RequestParam(value = "projectId") String projectId,
			@RequestParam(value = "sprintId", required = false) String sprintId,
			@RequestParam(value = "userId") String userId, @RequestParam(value = "meetingDate") String meetingDate,
			@RequestParam(value = "meetingMinutes") String meetingMinutes) {
		LOGGER.info("addMeetingNotesNext() | Adding new standup for project: {} for user: {} on date: {}", projectId,
				userId, meetingDate);
		standupService.addStandup(StandupEventType.NEXT, projectId, sprintId, userId, converterUtil.toDate(meetingDate),
				meetingMinutes);
		return new ResponseEntity<String>(
				converterUtil.toJson("Meeting notes for next days has been added successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/addMeetingNotesImpediment", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> addMeetingNotesImpediment(@RequestParam(value = "projectId") String projectId,
			@RequestParam(value = "sprintId", required = false) String sprintId,
			@RequestParam(value = "userId") String userId, @RequestParam(value = "meetingDate") String meetingDate,
			@RequestParam(value = "meetingMinutes") String meetingMinutes) {
		LOGGER.info("addMeetingNotesImpediment() | Adding new standup for project: {} for user: {} on date: {}",
				projectId, userId, meetingDate);
		standupService.addStandup(StandupEventType.IMPEDIMENT, projectId, sprintId, userId,
				converterUtil.toDate(meetingDate), meetingMinutes);
		return new ResponseEntity<String>(
				converterUtil.toJson("Meeting notes for impediments has been added successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateMeetingNotes", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updateMeetingNotes(@RequestParam(value = "id") String id,
			@RequestParam(value = "meetingDate") String meetingDate,
			@RequestParam(value = "meetingMinutes") String meetingMinutes) {
		LOGGER.info("updateMeetingNotes() | Updating the standup:{}", id);
		standupService.updateStandup(id, converterUtil.toDate(meetingDate), meetingMinutes);
		return new ResponseEntity<String>(converterUtil.toJson("Meeting notes has been updated successfully"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/sendMeetingMinutesEmail", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> sendMeetingMinutesEmail(@RequestParam(value = "projectId") String projectId) {
		LOGGER.info("sendMeetingMinutesEmail() | Send meeting minutes email for the project: {}", projectId);
		standupService.sendMeetingMinutesEmail(projectId);
		return new ResponseEntity<String>(converterUtil.toJson("Meeting notes has been sent successfully"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/sendReminderEmail", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> sendReminderEmail(@RequestParam(value = "userName") String userName) {
		LOGGER.info("sendReminderEmail() | Send reminder email for the user: {} to join the meeting", userName);
		standupService.sendReminderEmail(userName);
		return new ResponseEntity<String>(converterUtil.toJson("Meeting notes has been sent successfully"),
				HttpStatus.OK);
	}
}