package com.genesyslab.machi.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SprintMetrics {

	@Id
	private String id;
	private String projectId;
	private String userId;
	private String sprintId;
	private String sprintName;

	// Duration in hours
	private int sprintDuration;
	private int plannedHours;
	private int actualHours;

	private int plannedTickets;
	private int completedTickets;
	private int wipTickets;
	private int openTickets;
	private int blockedTickets;
	private int totalWeightage;
	private int userScorePercentage;

	private double userScore;

	private Date createDate;

	@Transient
	private int rank;
	@Transient
	private String userName;

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

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public int getSprintDuration() {
		return sprintDuration;
	}

	public void setSprintDuration(int sprintDuration) {
		this.sprintDuration = sprintDuration;
	}

	public int getPlannedHours() {
		return plannedHours;
	}

	public void setPlannedHours(int plannedHours) {
		this.plannedHours = plannedHours;
	}

	public int getActualHours() {
		return actualHours;
	}

	public void setActualHours(int actualHours) {
		this.actualHours = actualHours;
	}

	public int getPlannedTickets() {
		return plannedTickets;
	}

	public void setPlannedTickets(int plannedTickets) {
		this.plannedTickets = plannedTickets;
	}

	public int getCompletedTickets() {
		return completedTickets;
	}

	public void setCompletedTickets(int completedTickets) {
		this.completedTickets = completedTickets;
	}

	public int getWipTickets() {
		return wipTickets;
	}

	public void setWipTickets(int wipTickets) {
		this.wipTickets = wipTickets;
	}

	public int getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(int openTickets) {
		this.openTickets = openTickets;
	}

	public int getBlockedTickets() {
		return blockedTickets;
	}

	public void setBlockedTickets(int blockedTickets) {
		this.blockedTickets = blockedTickets;
	}

	public int getTotalWeightage() {
		return totalWeightage;
	}

	public void setTotalWeightage(int totalWeightage) {
		this.totalWeightage = totalWeightage;
	}

	public int getUserScorePercentage() {
		return userScorePercentage;
	}

	public void setUserScorePercentage(int userScorePercentage) {
		this.userScorePercentage = userScorePercentage;
	}

	public double getUserScore() {
		return userScore;
	}

	public void setUserScore(double userScore) {
		this.userScore = userScore;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}