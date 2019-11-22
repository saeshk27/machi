package com.genesyslab.machi.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Project {

	@Id
	private String id;
	private String name;
	private String currentSprint;
	private String dailyStandupTime;
	private String sprintId;
	private String productOwnerId;
	private List<String> memberIds;

	private Date dailyStandUp;
	private Date backlogGrooming;
	private Date sprintStartDate;
	private Date sprintEndDate;
	private Date createDate;
	private Date lastUpdateDate;

	@Transient
	private List<User> members;
	@Transient
	private String productOwnerName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentSprint() {
		return currentSprint;
	}

	public void setCurrentSprint(String currentSprint) {
		this.currentSprint = currentSprint;
	}

	public String getDailyStandupTime() {
		return dailyStandupTime;
	}

	public void setDailyStandupTime(String dailyStandupTime) {
		this.dailyStandupTime = dailyStandupTime;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getProductOwnerId() {
		return productOwnerId;
	}

	public void setProductOwnerId(String productOwnerId) {
		this.productOwnerId = productOwnerId;
	}

	public List<String> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<String> memberIds) {
		this.memberIds = memberIds;
	}

	public void addMemberId(String memberId) {
		memberIds.add(memberId);
	}

	public Date getDailyStandUp() {
		return dailyStandUp;
	}

	public void setDailyStandUp(Date dailyStandUp) {
		this.dailyStandUp = dailyStandUp;
	}

	public Date getBacklogGrooming() {
		return backlogGrooming;
	}

	public void setBacklogGrooming(Date backlogGrooming) {
		this.backlogGrooming = backlogGrooming;
	}

	public Date getSprintStartDate() {
		return sprintStartDate;
	}

	public void setSprintStartDate(Date sprintStartDate) {
		this.sprintStartDate = sprintStartDate;
	}

	public Date getSprintEndDate() {
		return sprintEndDate;
	}

	public void setSprintEndDate(Date sprintEndDate) {
		this.sprintEndDate = sprintEndDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public String getProductOwnerName() {
		return productOwnerName;
	}

	public void setProductOwnerName(String productOwnerName) {
		this.productOwnerName = productOwnerName;
	}
}