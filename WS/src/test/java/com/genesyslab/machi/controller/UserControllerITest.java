package com.genesyslab.machi.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.helper.MachiTestConstants;
import com.genesyslab.machi.service.UserService;
import com.genesyslab.machi.util.ConverterUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService mockUserService;
	@MockBean
	ConverterUtil mockConverterUtil;

	@BeforeEach
	void setUp() throws Exception {
		when(mockConverterUtil.toJson(contains("successfully"))).thenReturn(MachiTestConstants.TEST_JSON_MESSAGE);

		mockUser();
	}

	private void mockUser() {
		User testUser = new User();
		testUser.setId(MachiTestConstants.TEST_KEY);
		testUser.setLoginId(MachiTestConstants.TEST_LOGIN_ID);
		testUser.setPassword(MachiTestConstants.TEST_PASSWORD);

		when(mockUserService.login(MachiTestConstants.TEST_LOGIN_ID, MachiTestConstants.TEST_PASSWORD))
				.thenReturn(testUser);
	}

	@Test
	public void testShouldLogin() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_USER_LOGIN)
				.param(MachiTestConstants.PARAM_LOGIN_ID, MachiTestConstants.TEST_LOGIN_ID)
				.param(MachiTestConstants.PARAM_PASSWORD, MachiTestConstants.TEST_PASSWORD)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.loginId", equalTo(MachiTestConstants.TEST_LOGIN_ID)));
	}

	@Test
	public void testShouldNotLoginForInvalidLogin() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_USER_LOGIN)
				.param(MachiTestConstants.PARAM_LOGIN_ID, MachiTestConstants.TEST_LOGIN_ID)
				.param(MachiTestConstants.PARAM_PASSWORD, MachiTestConstants.TEST_BAD_PASSWORD)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void testShouldNotLoginWhenParameterMissing() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_USER_LOGIN)
				.param(MachiTestConstants.PARAM_PASSWORD, MachiTestConstants.TEST_BAD_PASSWORD)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldUpdatePassword() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_USER_PASSWORD_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_KEY)
				.param(MachiTestConstants.PARAM_PASSWORD, MachiTestConstants.TEST_PASSWORD)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}

	@Test
	public void testShouldNotUpdatePasswordWhenParameterMissing() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_USER_PASSWORD_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_KEY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldNotUpdatePasswordThrowMethodNotAllowedErrorWhenGet() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_USER_PASSWORD_UPDATE)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_KEY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testShouldResetPassword() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_USER_PASSWORD_RESET)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_KEY)
				.param(MachiTestConstants.PARAM_PASSWORD, MachiTestConstants.TEST_PASSWORD)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}

	@Test
	public void testShouldNotResetPasswordWhenParameterMissing() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_USER_PASSWORD_RESET).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldNotResetPasswordThrowMethodNotAllowedErrorWhenGet() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_USER_PASSWORD_RESET)
				.param(MachiTestConstants.PARAM_ID, MachiTestConstants.TEST_KEY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
	}
}
