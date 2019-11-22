package com.genesyslab.machi.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.genesyslab.machi.domain.type.StandupEventType;

@Document
public class Standup {

	@Id
	private String id;
	private String meetingMinutes;
	private String projectId;
	private String sprintId;
	private String userId;

	private Date meetingDate;

	private StandupEventType type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeetingMinutes() {
		return meetingMinutes;
	}

	public void setMeetingMinutes(String meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public StandupEventType getType() {
		return type;
	}

	public void setType(StandupEventType type) {
		this.type = type;
	}
}