package com.genesyslab.machi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.genesyslab.machi.dao.DBHealthCheckDAO;
import com.genesyslab.machi.domain.DBHealthCheck;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DBHealthCheckServiceTest {

	@Mock
	DBHealthCheckDAO mockDBHealthCheckDAO;

	@InjectMocks
	DBHealthCheckService dbHealthCheckService;

	DBHealthCheck dbHealthCheck = new DBHealthCheck();

	@Test
	public void testShouldBeHealthy() {
		dbHealthCheck.setHealthy(true);
		when(mockDBHealthCheckDAO.findFirstByHealthy(true)).thenReturn(dbHealthCheck);

		boolean response = dbHealthCheckService.isHealthy();

		assertTrue(response);
	}

	@Test
	public void testShouldNotBeHealthy() {
		dbHealthCheck.setHealthy(false);
		when(mockDBHealthCheckDAO.findFirstByHealthy(true)).thenReturn(dbHealthCheck);

		boolean response = dbHealthCheckService.isHealthy();

		assertFalse(response);
	}

}
