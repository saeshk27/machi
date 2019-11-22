package com.genesyslab.machi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.helper.MachiConstants;
import com.genesyslab.machi.service.UserService;
import com.genesyslab.machi.util.ConverterUtil;

@RestController
@RequestMapping("/machi/user")
public class UserController {

	public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	@Autowired
	ConverterUtil converterUtil;

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<User> login(@RequestParam(value = "loginId") String loginId,
			@RequestParam(value = "password") String password, HttpServletRequest request) {

		LOGGER.info("login() | Login attempted for user: {}", loginId);
		User user = userService.login(loginId, password);

		if (null != user) {
			request.getSession().setAttribute(MachiConstants.SESSION_USER_ID, user.getId());
			request.getSession().setAttribute(MachiConstants.SESSION_USER_NAME, user.getFullName());
			request.getSession().setAttribute(MachiConstants.SESSION_USER_PROJECT_ID, user.getProjectId());
			request.getSession().setAttribute(MachiConstants.SESSION_SESSION_ID, user.getSessionId());
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> updatePassword(@RequestParam(value = "id") String id,
			@RequestParam(value = "password") String password) {
		LOGGER.info("updatePassword() | Password update requested by user: {}", id);

		userService.updatePassword(id, password);
		return new ResponseEntity<String>(converterUtil.toJson("Password updated successfully"), HttpStatus.OK);
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> resetPassword(@RequestParam(value = "id") String id) {
		LOGGER.info("resetPassword() | Password reset requested by user: {}", id);

		userService.updatePassword(id, null);
		return new ResponseEntity<String>(converterUtil.toJson("Password reset successfully"), HttpStatus.OK);
	}
}