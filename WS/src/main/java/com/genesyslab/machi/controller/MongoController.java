package com.genesyslab.machi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.machi.util.ConverterUtil;
import com.genesyslab.machi.util.MongoUtil;

@RestController
@RequestMapping("/machi/mongo")
public class MongoController {

	public static final Logger LOGGER = LoggerFactory.getLogger(MongoController.class);

	@Autowired
	MongoUtil mongoUtil;
	@Autowired
	ConverterUtil converterUtil;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/load", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity loadDefaults() {

		LOGGER.info("Load default values to Mongo DB");
		try {
			mongoUtil.deleteAll();
			mongoUtil.loadDefaults();
		} catch (Exception e) {
			LOGGER.error("Exception caught while loading default values into MongoDB", e);
			return new ResponseEntity("Exception caught while loading default values into MongoDB",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(converterUtil.toJson("Default values loaded into the DB"), HttpStatus.OK);
	}
}