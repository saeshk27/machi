package com.genesyslab.machi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.machi.service.DBHealthCheckService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/health")
public class HealthCheckController {

	public static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);

	@Autowired
	DBHealthCheckService dbHealthCheckService;
	@Autowired
	ConverterUtil converterUtil;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/isHealthy", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity isHealthy() {

		if (dbHealthCheckService.isHealthy()) {
			LOGGER.info("Machi WebService is healthy");
			return new ResponseEntity(converterUtil.toJson("Machi Service is healthy"), HttpStatus.OK);
		}
		LOGGER.warn("Machi WebService is unavailable");
		return new ResponseEntity(converterUtil.toJson("Machi WebService is unavailable"), HttpStatus.NOT_FOUND);
	}
}