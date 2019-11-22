package com.genesyslab.machi.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.helper.MachiTestConstants;
import com.genesyslab.machi.service.ProjectService;
import com.genesyslab.machi.util.ConverterUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc
class ProjectControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ProjectService mockProjectService;
	@MockBean
	ConverterUtil mockConverterUtil;

	@BeforeEach
	void setUp() throws Exception {
		when(mockConverterUtil.toJson(contains("successfully"))).thenReturn(MachiTestConstants.TEST_JSON_MESSAGE);

		mockProject();
	}

	private void mockProject() {
		Project testProject = new Project();
		testProject.setId(MachiTestConstants.TEST_PROJECT_ID);
		testProject.setName(MachiTestConstants.TEST_NAME);
		testProject.setCurrentSprint(MachiTestConstants.TEST_SPRINT);
		testProject.setProductOwnerId(MachiTestConstants.TEST_USER);
		testProject.setSprintId(MachiTestConstants.TEST_SPRINT_ID);
		testProject.setSprintStartDate(new Date());
		testProject.setSprintEndDate(new Date());
		testProject.setBacklogGrooming(new Date());
		testProject.setMemberIds(Arrays.asList(MachiTestConstants.TEST_USER));

		when(mockProjectService.getProjectDetails(MachiTestConstants.TEST_PROJECT_ID)).thenReturn(testProject);
		when(mockProjectService.getProjectsForProductOwner(MachiTestConstants.TEST_USER))
				.thenReturn(Arrays.asList(testProject));
	}

	@Test
	public void testShouldGetProjectDetails() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_DETAILS)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_PROJECT_ID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(MachiTestConstants.TEST_NAME)));
	}

	@Test
	public void testShouldNotGetProjectDetailsForInvalidInput() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_DETAILS)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_BAD_KEY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void testShouldNotGetProjectDetailsWhenParameterMissing() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_DETAILS).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldGetOwnerProjects() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_OWNER_PROJECTS)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0]name", equalTo(MachiTestConstants.TEST_NAME)));
	}

	@Test
	public void testShouldNotGetOwnerProjectsForInvalidInput() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_OWNER_PROJECTS)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_BAD_KEY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0]").doesNotExist());
	}

	@Test
	public void testShouldNotGetOwnerProjectsWhenParameterMissing() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_GET_OWNER_PROJECTS).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldAddProject() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_PROJECT_ADD)
				.param(MachiTestConstants.PARAM_NAME, MachiTestConstants.TEST_NAME)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}

	@Test
	public void testShouldNotAddProjectWhenParameterMissing() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_PROJECT_ADD)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldNotAddProjectThrowMethodNotAllowedErrorWhenGet() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_ADD)
				.param(MachiTestConstants.PARAM_NAME, MachiTestConstants.TEST_NAME)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testShouldUpdateProject() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_PROJECT_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_PROJECT_ID)
				.param(MachiTestConstants.PARAM_NAME, MachiTestConstants.TEST_NAME)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}

	@Test
	public void testShouldNotUpdateProjectWhenParameterMissing() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_PROJECT_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_PROJECT_ID)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldNotUpdateProjectThrowMethodNotAllowedErrorWhenGet() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_PROJECT_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_PROJECT_ID)
				.param(MachiTestConstants.PARAM_NAME, MachiTestConstants.TEST_NAME)
				.param(MachiTestConstants.PARAM_PRODUCT_OWNER_ID, MachiTestConstants.TEST_USER)
				.param(MachiTestConstants.PARAM_MEMBER_IDS, MachiTestConstants.TEST_MEMBER_IDS)
				.param(MachiTestConstants.PARAM_CURRENT_SPRINT, MachiTestConstants.TEST_SPRINT)
				.param(MachiTestConstants.PARAM_SPRINT_ID, MachiTestConstants.TEST_SPRINT_ID)
				.param(MachiTestConstants.PARAM_BACKLOG_GROOMING, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_DAILY_STANDUP, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_SPRINT_START_DATE, MachiTestConstants.TEST_DATE)
				.param(MachiTestConstants.PARAM_END_DATE, MachiTestConstants.TEST_DATE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
	}
}
