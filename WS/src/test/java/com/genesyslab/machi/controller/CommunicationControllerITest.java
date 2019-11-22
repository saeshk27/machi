package com.genesyslab.machi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
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

import com.genesyslab.machi.helper.MachiTestConstants;
import com.genesyslab.machi.service.communication.EmailService;
import com.genesyslab.machi.util.ConverterUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommunicationController.class)
@AutoConfigureMockMvc
class CommunicationControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	EmailService mockEmailService;
	@MockBean
	ConverterUtil mockConverterUtil;

	@BeforeEach
	public void setUp() throws Exception {
		when(mockConverterUtil.toJson(contains("successfully"))).thenReturn(MachiTestConstants.TEST_JSON_MESSAGE);

		doNothing().when(mockEmailService).sendMail(MachiTestConstants.TEST_SUBJECT, MachiTestConstants.TEST_BODY,
				MachiTestConstants.TEST_TO_ID, MachiTestConstants.TEST_CC_ID);
	}

	@Test
	public void testShouldSendMail() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_SEND_MAIL)
				.param(MachiTestConstants.PARAM_TO_ID, MachiTestConstants.TEST_TO_ID)
				.param(MachiTestConstants.PARAM_CC_ID, MachiTestConstants.TEST_CC_ID)
				.param(MachiTestConstants.PARAM_BODY, MachiTestConstants.TEST_BODY)
				.param(MachiTestConstants.PARAM_SUBJECT, MachiTestConstants.TEST_SUBJECT)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}

	@Test
	public void testShouldNotSendMailWhenParameterMissing() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_SEND_MAIL)
				.param(MachiTestConstants.PARAM_TO_ID, MachiTestConstants.TEST_TO_ID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldNotSendMailThrowMethodNotAllowedErrorWhenGet() throws Exception {
		mockMvc.perform(get(MachiTestConstants.URL_SEND_MAIL)
				.param(MachiTestConstants.PARAM_TO_ID, MachiTestConstants.TEST_TO_ID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
	}
}
