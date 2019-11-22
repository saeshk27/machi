package com.genesyslab.machi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.DBHealthCheckDAO;
import com.genesyslab.machi.domain.DBHealthCheck;

@Service
public class DBHealthCheckService {

	public static final Logger LOGGER = LoggerFactory.getLogger(DBHealthCheckService.class);

	@Autowired
	DBHealthCheckDAO dbHealthCheckDAO;

	public boolean isHealthy() {
		try {
			DBHealthCheck dbHealthCheck = dbHealthCheckDAO.findFirstByHealthy(true);
			return dbHealthCheck.isHealthy();
		} catch (Exception ex) {
			LOGGER.error("Service is unavailable. Exception caught: ", ex);
		}
		return false;
	}
}
