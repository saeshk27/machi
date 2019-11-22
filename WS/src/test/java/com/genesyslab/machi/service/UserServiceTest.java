package com.genesyslab.machi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.genesyslab.machi.dao.UserDAO;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.helper.MachiTestConstants;
import com.genesyslab.machi.util.ConverterUtil;
import com.genesyslab.machi.util.PasswordUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class UserServiceTest {

	@Mock
	UserDAO mockUserDAO;
	@Mock
	PasswordUtil mockPasswordUtil;
	@Mock
	ConverterUtil mockConverterUtil;
	@Mock
	User mockUser;

	@InjectMocks
	UserService userService;

	@BeforeEach
	public void setUp() throws Exception {
		mockUser();
		mockConverterUtil();
		mockPasswordUtil();
	}

	private void mockPasswordUtil() {
		when(mockPasswordUtil.getEncryptedPassword(MachiTestConstants.TEST_PASSWORD))
				.thenReturn(MachiTestConstants.TEST_ENCRYPTED_PASSWORD);
	}

	private void mockConverterUtil() {
		Date today = new Date();
		when(mockConverterUtil.toDate(MachiTestConstants.TEST_DATE)).thenReturn(today);
	}

	private void mockUser() {
		mockUser = new User();
		mockUser.setId(MachiTestConstants.TEST_KEY);
		mockUser.setLoginId(MachiTestConstants.TEST_LOGIN_ID);
		mockUser.setPassword(MachiTestConstants.TEST_ENCRYPTED_PASSWORD);
		mockUser.setEmail(MachiTestConstants.TEST_EMAIL);
		mockUser.setProjectId(MachiTestConstants.TEST_PROJECT_ID);
		mockUser.setReportingId(MachiTestConstants.TEST_REPORTING_ID);
		mockUser.setDesignation(MachiTestConstants.TEST_DESIGNATION);

		Optional<User> optionalUser = Optional.of(mockUser);

		when(mockUserDAO.findById(MachiTestConstants.TEST_KEY)).thenReturn(optionalUser);
		when(mockUserDAO.findById(MachiTestConstants.TEST_USER)).thenReturn(optionalUser);
		when(mockUserDAO.findByEmailIgnoreCase(MachiTestConstants.TEST_EMAIL)).thenReturn(mockUser);
		when(mockUserDAO.save(mockUser)).thenReturn(mockUser);
		when(mockUserDAO.findByLoginIdIgnoreCaseAndPassword(MachiTestConstants.TEST_LOGIN_ID,
				MachiTestConstants.TEST_ENCRYPTED_PASSWORD)).thenReturn(mockUser);
		when(mockUserDAO.findByProjectId(MachiTestConstants.TEST_PROJECT_ID)).thenReturn(Arrays.asList(mockUser));
		when(mockUserDAO.findByReportingId(MachiTestConstants.TEST_REPORTING_ID)).thenReturn(Arrays.asList(mockUser));
	}

	@Test
	public void testShouldLogin() {
		User responseUser = userService.login(MachiTestConstants.TEST_LOGIN_ID, MachiTestConstants.TEST_PASSWORD);

		validateResponse(responseUser);
		assertNotNull(responseUser.getSessionId());
		assertNotNull(responseUser.getLastLoginDate());
	}

	@Test
	public void testShouldNotLogin() {
		User responseUser = userService.login(MachiTestConstants.TEST_BAD_LOGIN_ID, MachiTestConstants.TEST_PASSWORD);

		assertNull(responseUser);
	}

	@Test
	public void testShouldNotLoginWhenBadPassword() {
		User responseUser = userService.login(MachiTestConstants.TEST_LOGIN_ID, MachiTestConstants.TEST_BAD_PASSWORD);

		assertNull(responseUser);
	}

	@Test
	public void testShouldGetUser() {
		User responseUser = userService.getUser(MachiTestConstants.TEST_KEY);

		validateResponse(responseUser);
	}

	@Test
	public void testShouldNotGetUser() {
		User responseUser = userService.getUser(MachiTestConstants.TEST_BAD_KEY);

		assertNull(responseUser);
	}

	@Test
	public void testShouldGetUserByEmail() {
		User responseUser = userService.getUserByEmail(MachiTestConstants.TEST_EMAIL);

		validateResponse(responseUser);
	}

	@Test
	public void testShouldNotGetUserByEmail() {
		User responseUser = userService.getUserByEmail(MachiTestConstants.TEST_BAD_EMAIL);

		assertNull(responseUser);
	}

	@Test
	public void testShouldGetUsersForProject() {
		List<User> responseUsers = userService.getUsersForProject(MachiTestConstants.TEST_PROJECT_ID);

		validateResponse(responseUsers);
	}

	@Test
	public void testShouldNotGetUsersForProject() {
		List<User> responseUsers = userService.getUsersForProject(MachiTestConstants.TEST_BAD_KEY);

		assertTrue(responseUsers.isEmpty());
	}

	@Test
	public void testShouldGetUsersForReportingId() {
		List<User> responseUsers = userService.getUsersForReportingId(MachiTestConstants.TEST_REPORTING_ID);

		validateResponse(responseUsers);
	}

	@Test
	public void testShouldNotGetUsersForReportingId() {
		List<User> responseUsers = userService.getUsersForReportingId(MachiTestConstants.TEST_BAD_KEY);

		assertTrue(responseUsers.isEmpty());
	}

	private void validateResponse(List<User> responseUsers) {
		assertNotNull(responseUsers);

		assertTrue(responseUsers.size() > 0);

		validateResponse(responseUsers.get(0));
	}

	private void validateResponse(User responseUser) {
		assertNotNull(responseUser);

		assertEquals(responseUser.getId(), MachiTestConstants.TEST_KEY);
		assertEquals(responseUser.getLoginId(), MachiTestConstants.TEST_LOGIN_ID);
		assertEquals(responseUser.getPassword(), MachiTestConstants.TEST_ENCRYPTED_PASSWORD);
		assertEquals(responseUser.getEmail(), MachiTestConstants.TEST_EMAIL);
	}

	@Test
	public void testShouldGenerateSessionId() {
		String response = userService.generateSessionId();

		assertNotNull(response);
		assertTrue(response.startsWith("S"));
	}

	@Test
	void testShouldUpdateReporting() {
		userService.updateReporting(MachiTestConstants.TEST_KEY, MachiTestConstants.TEST_USER);

		verify(mockUserDAO).save(mockUser);
	}

	@Test
	void testShouldNotUpdateReporting() {
		userService.updateReporting(MachiTestConstants.TEST_BAD_KEY, MachiTestConstants.TEST_USER);

		verify(mockUserDAO, never()).save(mockUser);
	}

	@Test
	void testShouldUpdatePassword() {
		userService.updatePassword(MachiTestConstants.TEST_KEY, MachiTestConstants.TEST_PASSWORD);

		verify(mockUserDAO).save(mockUser);
	}

	@Test
	void testShouldUpdatePasswordWhenNullPassword() {
		userService.updatePassword(MachiTestConstants.TEST_KEY, null);

		verify(mockUserDAO).save(mockUser);
	}

	@Test
	void testShouldNotUpdatePassword() {
		userService.updatePassword(MachiTestConstants.TEST_BAD_KEY, MachiTestConstants.TEST_PASSWORD);

		verify(mockUserDAO, never()).save(mockUser);
	}

	@Test
	void testShouldUpdateProjectForUsers() {
		userService.updateProjectForUsers(MachiTestConstants.TEST_PROJECT_ID,
				Arrays.asList(MachiTestConstants.TEST_USER));

		verify(mockUserDAO, times(2)).saveAll(anyList());
	}
}
