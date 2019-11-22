package com.genesyslab.machi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
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
import com.genesyslab.machi.util.ConverterUtil;
import com.genesyslab.machi.util.MongoUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MongoController.class)
@AutoConfigureMockMvc
class MongoControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MongoUtil mockMongoUtil;
	@MockBean
	ConverterUtil mockConverterUtil;

	@BeforeEach
	void setUp() throws Exception {
		when(mockConverterUtil.toJson(contains("loaded"))).thenReturn(MachiTestConstants.TEST_JSON_MESSAGE);

		doNothing().when(mockMongoUtil).deleteAll();
		doNothing().when(mockMongoUtil).loadDefaults();
	}
	
	@Test
	public void testShouldLoadDefaults() throws Exception {
		mockMvc.perform(post(MachiTestConstants.URL_MONGO_LOAD_DEFAULTS)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(equalTo(MachiTestConstants.TEST_JSON_MESSAGE)));
	}
	
	@Test
	public void testShouldNotLoadDefaults() throws Exception {
		doThrow(Exception.class).when(mockMongoUtil).deleteAll();
		
		mockMvc.perform(post(MachiTestConstants.URL_MONGO_LOAD_DEFAULTS)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$", Matchers.startsWith("Exception")));
	}
	
	@Test
	public void testLoadDefaultsShouldThrowMethodNotAllowedErrorWhenGet() throws Exception {
		doThrow(Exception.class).when(mockMongoUtil).deleteAll();
		
		mockMvc.perform(get(MachiTestConstants.URL_MONGO_LOAD_DEFAULTS)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$").doesNotExist());
	}
}
