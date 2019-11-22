package com.genesyslab.machi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.machi.service.communication.EmailService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/communication")
public class CommunicationController {

	public static final Logger LOGGER = LoggerFactory.getLogger(CommunicationController.class);

	@Autowired
	EmailService emailService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> sendMail(@RequestParam(value = "toId") String toId,
			@RequestParam(value = "ccId") String ccId, @RequestParam(value = "subject") String subject,
			@RequestParam(value = "body") String body) {

		LOGGER.info("sendMail() | toId: {}, ccId: {}, subject: {}, body: {}", toId, ccId, subject, body);
		emailService.sendMail(subject, body, toId, ccId);
		return new ResponseEntity<String>(converterUtil.toJson("Mail sent successfully"), HttpStatus.OK);
	}
}