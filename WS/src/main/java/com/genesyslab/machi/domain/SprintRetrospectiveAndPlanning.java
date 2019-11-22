package com.genesyslab.machi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SprintRetrospectiveAndPlanning {

	@Id
	private String id;
	private String projectId;
	private String userId;
	private String sprintName;

	private String wentWell;
	private String didNotGoWell;
	private String learned;
	private String puzzle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public String getWentWell() {
		return wentWell;
	}

	public void setWentWell(String wentWell) {
		this.wentWell = wentWell;
	}

	public String getDidNotGoWell() {
		return didNotGoWell;
	}

	public void setDidNotGoWell(String didNotGoWell) {
		this.didNotGoWell = didNotGoWell;
	}

	public String getLearned() {
		return learned;
	}

	public void setLearned(String learned) {
		this.learned = learned;
	}

	public String getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(String puzzle) {
		this.puzzle = puzzle;
	}
}