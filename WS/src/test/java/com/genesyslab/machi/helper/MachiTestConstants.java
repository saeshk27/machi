package com.genesyslab.machi.helper;

import java.util.Date;

public class MachiTestConstants {

	public static final String URL_BASE = "/machi";
	public static final String URL_HEALTHCHECK = URL_BASE + "/health/isHealthy";
	public static final String URL_MONGO_LOAD_DEFAULTS = URL_BASE + "/mongo/load";
	public static final String URL_SEND_MAIL = URL_BASE + "/communication/sendMail";
	public static final String URL_USER_LOGIN = URL_BASE + "/user/login";
	public static final String URL_USER_PASSWORD_UPDATE = URL_BASE + "/user/updatePassword";
	public static final String URL_USER_PASSWORD_RESET = URL_BASE + "/user/resetPassword";
	public static final String URL_PROJECT_GET_DETAILS = URL_BASE + "/project/getProjectDetails";
	public static final String URL_PROJECT_GET_OWNER_PROJECTS = URL_BASE + "/project/getOwnerProjects";
	public static final String URL_PROJECT_ADD = URL_BASE + "/project/addProject";
	public static final String URL_PROJECT_UPDATE = URL_BASE + "/project/updateProject";

	public static final String TEST_KEY = "Test ID";
	public static final String TEST_JSON_MESSAGE = "Test Json Message";
	public static final String TEST_DATE = "19880327";
	public static final String TEST_TIME = "12:34";

	public static final String TEST_USER = "Test User ID";
	public static final String TEST_LOGIN_ID = "Test Login ID";
	public static final String TEST_PASSWORD = "Test Password";
	public static final String TEST_ENCRYPTED_PASSWORD = "Test Encrypted Password";
	public static final String TEST_MOBILE = "Test Mobile Number";
	public static final String TEST_EMAIL = "TestEmailId@email.com";
	public static final String TEST_DESIGNATION = "Test Designation";
	public static final String TEST_REPORTING_ID = "Test Reporting";
	public static final String TEST_PROJECT_ID = "Test Project";

	public static final String TEST_SUBJECT = "Test Subject";
	public static final String TEST_BODY = "Test Body";
	public static final String TEST_FROM_ID = "TestFromId@email.com";
	public static final String TEST_TO_ID = "TestToId@email.com";
	public static final String TEST_CC_ID = "TestCcId@email.com";

	public static final String TEST_NAME = "Test Name";
	public static final String TEST_SPRINT = "Test Spring";
	public static final String TEST_SPRINT_ID = "Test Spring ID";
	public static final String TEST_MEMBER_IDS = "Test Member ID 1,Test Member ID 2,Test Member ID 3";
	
	public static final Date TEST_TODAY_DATE = new Date();

	public static final String TEST_BAD_KEY = "Test Bad ID";
	public static final String TEST_BAD_DATE = "Test Bad Date";
	public static final String TEST_BAD_TIME = "Test Bad Time";
	public static final String TEST_BAD_LOGIN_ID = "Test Incorrect Login ID";
	public static final String TEST_BAD_PASSWORD = "Test Bad Password";
	public static final String TEST_BAD_EMAIL = "TestIncorrectEmailID@email.com";

	public static final String PARAM_ID = "id";
	public static final String PARAM_MESSAGE = "message";
	public static final String PARAM_TO_ID = "toId";
	public static final String PARAM_CC_ID = "ccId";
	public static final String PARAM_SUBJECT = "subject";
	public static final String PARAM_BODY = "body";
	public static final String PARAM_LOGIN_ID = "loginId";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_PRODUCT_OWNER_ID = "productOwnerId";
	public static final String PARAM_MEMBER_IDS = "memberIds";
	public static final String PARAM_CURRENT_SPRINT = "currentSprint";
	public static final String PARAM_SPRINT_ID = "sprintId";
	public static final String PARAM_BACKLOG_GROOMING = "backlogGrooming";
	public static final String PARAM_DAILY_STANDUP = "dailyStandUp";
	public static final String PARAM_SPRINT_START_DATE = "sprintStartDate";
	public static final String PARAM_END_DATE = "sprintEndDate";

}