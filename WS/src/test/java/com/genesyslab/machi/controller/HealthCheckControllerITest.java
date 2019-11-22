package com.genesyslab.machi.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.genesyslab.machi.service.DBHealthCheckService;
import com.genesyslab.machi.util.ConverterUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthCheckController.class)
@AutoConfigureMockMvc
class HealthCheckControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DBHealthCheckService mockDBHealthCheckService;
	@MockBean
	ConverterUtil mockConverterUtil;

	@BeforeEach
	public void setUp() throws Exception {
		when(mockConverterUtil.toJson(contains("healthy"))).thenReturn(MachiTestConstants.TEST_JSON_MESSAGE);
	}
	
	@Test
	public void testShouldBeHealthy() throws Exception {
		when(mockDBHealthCheckService.isHealthy()).thenReturn(true);
		
		mockMvc.perform(get(MachiTestConstants.URL_HEALTHCHECK)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}
	
	@Test
	public void testShouldNotBeHealthy() throws Exception {
		when(mockDBHealthCheckService.isHealthy()).thenReturn(false);
		
		mockMvc.perform(get(MachiTestConstants.URL_HEALTHCHECK)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist());
	}
}
