package com.genesyslab.machi.service.sprint;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.SprintRetrospectiveAndPlanningDAO;
import com.genesyslab.machi.domain.SprintRetrospectiveAndPlanning;

@Service
public class SprintRetrospectiveAndPlanningService {

	public static final Logger LOGGER = LoggerFactory.getLogger(SprintRetrospectiveAndPlanningService.class);

	@Autowired
	SprintRetrospectiveAndPlanningDAO sprintRetrospectiveAndPlanningDAO;

	public SprintRetrospectiveAndPlanning getSprintRetrospectiveAndPlanning(String id) {
		return sprintRetrospectiveAndPlanningDAO.findById(id).orElse(null);
	}

	public List<SprintRetrospectiveAndPlanning> getCommentsForSprint(String sprintName) {
		return sprintRetrospectiveAndPlanningDAO.findBySprintNameOrderByUserId(sprintName);
	}

	public List<SprintRetrospectiveAndPlanning> getCommentsForProject(String projectId) {
		return sprintRetrospectiveAndPlanningDAO.findByProjectIdOrderByUserId(projectId);
	}

	public void addUserComments(String userId, String projectId, String sprintName, String wentWell,
			String didNotGoWell, String learned, String puzzle) {
		LOGGER.info("addUserComments() | Adding/updating user: {} comments for sprint: {}", userId, sprintName);
		SprintRetrospectiveAndPlanning sprintReP = sprintRetrospectiveAndPlanningDAO.findByUserId(userId);
		if (sprintReP == null) {
			sprintReP = new SprintRetrospectiveAndPlanning();
			sprintReP.setProjectId(projectId);
			sprintReP.setSprintName(sprintName);
			sprintReP.setUserId(userId);
		}
		if (StringUtils.isNotBlank(wentWell))
			sprintReP.setWentWell(wentWell);
		if (StringUtils.isNotBlank(didNotGoWell))
			sprintReP.setDidNotGoWell(didNotGoWell);
		if (StringUtils.isNotBlank(learned))
			sprintReP.setLearned(learned);
		if (StringUtils.isNotBlank(puzzle))
			sprintReP.setPuzzle(puzzle);

		sprintRetrospectiveAndPlanningDAO.save(sprintReP);
	}
}