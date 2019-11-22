package com.genesyslab.machi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import com.genesyslab.machi.dao.ProjectDAO;
import com.genesyslab.machi.domain.Project;
import com.genesyslab.machi.domain.Standup;
import com.genesyslab.machi.domain.User;
import com.genesyslab.machi.helper.MachiTestConstants;
import com.genesyslab.machi.service.sprint.StandupService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class ProjectServiceTest {

	@Mock
	ProjectDAO mockProjectDAO;
	@Mock
	UserService mockUserService;
	@Mock
	StandupService mockStandupService;
	@Mock
	Project mockProject;
	@Mock
	User mockUser;
	@Mock
	Standup mockStandup;

	@InjectMocks
	ProjectService projectService;

	@BeforeEach
	public void setUp() throws Exception {
		mockProject();
		mockUser();
		mockStandup();
	}

	private void mockProject() {
		mockProject = new Project();
		mockProject.setId(MachiTestConstants.TEST_PROJECT_ID);
		mockProject.setName(MachiTestConstants.TEST_NAME);
		mockProject.setCurrentSprint(MachiTestConstants.TEST_SPRINT);
		mockProject.setProductOwnerId(MachiTestConstants.TEST_USER);
		mockProject.setSprintId(MachiTestConstants.TEST_SPRINT_ID);
		mockProject.setSprintStartDate(new Date());
		mockProject.setSprintEndDate(new Date());
		mockProject.setBacklogGrooming(new Date());
		mockProject.setMemberIds(Arrays.asList(MachiTestConstants.TEST_USER));

		Optional<Project> optionalProject = Optional.of(mockProject);

		when(mockProjectDAO.findById(MachiTestConstants.TEST_PROJECT_ID)).thenReturn(optionalProject);
		when(mockProjectDAO.findByProductOwnerId(MachiTestConstants.TEST_USER)).thenReturn(Arrays.asList(mockProject));
		when(mockProjectDAO.save(any(Project.class))).thenReturn(mockProject);
	}

	private void mockUser() {
		mockUser = new User();
		mockUser.setId(MachiTestConstants.TEST_USER);
		mockUser.setLoginId(MachiTestConstants.TEST_LOGIN_ID);
		mockUser.setEmail(MachiTestConstants.TEST_EMAIL);
		mockUser.setProjectId(MachiTestConstants.TEST_PROJECT_ID);
		mockUser.setReportingId(MachiTestConstants.TEST_REPORTING_ID);
		mockUser.setDesignation(MachiTestConstants.TEST_DESIGNATION);

		when(mockUserService.getUsersForProject(MachiTestConstants.TEST_PROJECT_ID))
				.thenReturn(Arrays.asList(mockUser));
		when(mockUserService.getUser(MachiTestConstants.TEST_USER)).thenReturn(mockUser);
	}

	private void mockStandup() {
		mockStandup = new Standup();
		mockStandup.setId(MachiTestConstants.TEST_KEY);

		when(mockStandupService.getCurrentStandupsForUser(MachiTestConstants.TEST_USER))
				.thenReturn(Arrays.asList(mockStandup));
	}

	@Test
	public void testShouldGetProject() {
		Project responseProject = projectService.getProject(MachiTestConstants.TEST_PROJECT_ID);

		validateResponse(responseProject);
	}

	@Test
	public void testShouldNotGetProject() {
		Project responseProject = projectService.getProject(MachiTestConstants.TEST_BAD_KEY);

		assertNull(responseProject);
	}

	@Test
	public void testShouldGetProjectsForProductOwner() {
		List<Project> responseProjects = projectService.getProjectsForProductOwner(MachiTestConstants.TEST_USER);

		validateResponse(responseProjects);
	}

	@Test
	public void testShouldNotGetProjectsForProductOwner() {
		List<Project> responseProjects = projectService.getProjectsForProductOwner(MachiTestConstants.TEST_BAD_KEY);

		assertTrue(responseProjects.isEmpty());
	}

	@Test
	public void testShouldGetProjectDetails() {
		Project responseProject = projectService.getProjectDetails(MachiTestConstants.TEST_PROJECT_ID);

		validateResponse(responseProject);
		validateUser(responseProject.getMembers());
	}

	@Test
	public void testShouldNotGetProjectDetails() {
		Project responseProject = projectService.getProjectDetails(MachiTestConstants.TEST_BAD_KEY);

		assertNull(responseProject);
	}

	private void validateResponse(List<Project> responseProjects) {
		assertNotNull(responseProjects);

		assertTrue(responseProjects.size() > 0);

		validateResponse(responseProjects.get(0));
	}

	private void validateResponse(Project responseProject) {
		assertNotNull(responseProject);

		assertEquals(responseProject.getId(), MachiTestConstants.TEST_PROJECT_ID);
		assertEquals(responseProject.getName(), MachiTestConstants.TEST_NAME);
		assertEquals(responseProject.getSprintId(), MachiTestConstants.TEST_SPRINT_ID);
		assertEquals(responseProject.getCurrentSprint(), MachiTestConstants.TEST_SPRINT);
	}

	private void validateUser(List<User> responseUsers) {
		assertNotNull(responseUsers);
		assertTrue(responseUsers.size() > 0);

		User myUser = responseUsers.get(0);
		assertNotNull(myUser);
		assertEquals(myUser.getId(), MachiTestConstants.TEST_USER);
		assertEquals(myUser.getLoginId(), MachiTestConstants.TEST_LOGIN_ID);
		assertEquals(myUser.getEmail(), MachiTestConstants.TEST_EMAIL);
		assertEquals(myUser.getProjectId(), MachiTestConstants.TEST_PROJECT_ID);

		assertNotNull(myUser.getMeetingNotes());
		assertTrue(!myUser.getMeetingNotes().isEmpty());
	}

	@Test
	void testShouldAddProject() {
		projectService.addProject(MachiTestConstants.TEST_NAME, MachiTestConstants.TEST_USER,
				Arrays.asList(MachiTestConstants.TEST_USER), MachiTestConstants.TEST_SPRINT,
				MachiTestConstants.TEST_SPRINT_ID, new Date(), new Date(), new Date(), new Date());

		verify(mockProjectDAO).save(any(Project.class));
		verify(mockUserService).updateProjectForUsers(MachiTestConstants.TEST_PROJECT_ID,
				Arrays.asList(MachiTestConstants.TEST_USER));
	}

	@Test
	void testShouldUpdateProject() {
		projectService.updateProject(MachiTestConstants.TEST_PROJECT_ID, MachiTestConstants.TEST_NAME,
				MachiTestConstants.TEST_USER, Arrays.asList(MachiTestConstants.TEST_USER),
				MachiTestConstants.TEST_SPRINT, MachiTestConstants.TEST_SPRINT_ID, null, null, null, null);

		verify(mockProjectDAO).save(mockProject);
		verify(mockUserService).updateProjectForUsers(MachiTestConstants.TEST_PROJECT_ID,
				Arrays.asList(MachiTestConstants.TEST_USER));
	}

	@Test
	void testShouldNotUpdateProject() {
		projectService.updateProject(MachiTestConstants.TEST_BAD_KEY, MachiTestConstants.TEST_NAME,
				MachiTestConstants.TEST_USER, Arrays.asList(MachiTestConstants.TEST_USER),
				MachiTestConstants.TEST_SPRINT, MachiTestConstants.TEST_SPRINT_ID, null, null, null, null);

		verify(mockProjectDAO, never()).save(mockProject);
		verify(mockUserService, never()).updateProjectForUsers(MachiTestConstants.TEST_PROJECT_ID,
				Arrays.asList(MachiTestConstants.TEST_USER));
	}
}
