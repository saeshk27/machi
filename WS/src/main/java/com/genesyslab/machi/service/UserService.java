package com.genesyslab.machi.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.UserDAO;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.util.ConverterUtil;
import com.genesyslab.machi.util.PasswordUtil;

@Service
public class UserService {

	public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Value("${user.default.password}")
	private String defaultPassword;

	@Autowired
	UserDAO userDAO;
	@Autowired
	PasswordUtil passwordUtil;
	@Autowired
	ConverterUtil converterUtil;

	public User login(String loginId, String password) {
		User user = userDAO.findByLoginIdIgnoreCaseAndPassword(loginId, passwordUtil.getEncryptedPassword(password));
		if (null != user) {
			user.setLastLoginDate(new Date());
			userDAO.save(user);
			user.setSessionId(generateSessionId());
			LOGGER.info("login() | {} logged in successfully with loginId: {}, sessionId: {}", user.getFullName(),
					user.getLoginId(), user.getSessionId());
		}
		return user;
	}

	public User getUser(String id) {
		return userDAO.findById(id).orElse(null);
	}

	public User getUserByEmail(String email) {
		return userDAO.findByEmailIgnoreCase(email);
	}

	public String generateSessionId() {
		DateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
		return "S" + format.format(new Date());
	}

	public List<User> getUsersForProject(String projectId) {
		return userDAO.findByProjectId(projectId);
	}

	public List<User> getUsersForReportingId(String reportingId) {
		return userDAO.findByReportingId(reportingId);
	}

	public void updateReporting(String userId, String reportingId) {
		User user = getUser(userId);
		if (null != user) {
			LOGGER.info(
					"updateReporting() | Updating reporting of the user:{} from reportingId: {}, to new reportingId: {}",
					user.getFullName(), user.getReportingId(), reportingId);
			user.setReportingId(reportingId);
			userDAO.save(user);
		}
	}

	public void updatePassword(String id, String password) {
		password = StringUtils.isBlank(password) ? defaultPassword : password;
		User myUser = getUser(id);
		if (null != myUser) {
			myUser.setPassword(password);
			userDAO.save(myUser);
		}
	}

	public void updateProjectForUsers(String projectId, List<String> members) {
		// Remove all the existing users from project
		List<User> users = getUsersForProject(projectId).stream().peek(u -> u.setProjectId(null))
				.collect(Collectors.toList());
		userDAO.saveAll(users);

		// Add the new set of users to the project
		users = userDAO.findByIdIn(members).stream().peek(u -> u.setProjectId(projectId)).collect(Collectors.toList());
		userDAO.saveAll(users);
	}
}