package com.genesyslab.machi.service.sprint;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesyslab.machi.dao.BacklogGroomingDAO;
import com.genesyslab.machi.domain.BacklogGrooming;

@Service
public class BacklogGroomingService {

	public static final Logger LOGGER = LoggerFactory.getLogger(BacklogGroomingService.class);

	@Autowired
	BacklogGroomingDAO backlogGroomingDAO;

	public BacklogGrooming getBacklogGrooming(String id) {
		return backlogGroomingDAO.findById(id).orElse(null);
	}

	public List<BacklogGrooming> getBacklogsForSprint(String sprintId) {
		return backlogGroomingDAO.findBySprintIdOrderByTicketNo(sprintId);
	}

	public List<BacklogGrooming> getBacklogsForProject(String projectId) {
		return backlogGroomingDAO.findByProjectIdOrderByTicketNo(projectId);
	}

	public void updateBacklog(String id, String userId, String eta) {
		LOGGER.info("updateBacklog() | Updating backlog to user: {} for eta: {}", userId, eta);
		BacklogGrooming backlogGrooming = getBacklogGrooming(id);
		if (null != backlogGrooming) {
			if (StringUtils.isNotBlank(userId))
				backlogGrooming.setUserId(userId);
			if (StringUtils.isNotBlank(eta))
				backlogGrooming.setEta(eta);

			backlogGroomingDAO.save(backlogGrooming);
		}
	}
}