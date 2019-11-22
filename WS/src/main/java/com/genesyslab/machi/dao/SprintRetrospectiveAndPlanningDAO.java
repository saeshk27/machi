package com.genesyslab.machi.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.SprintRetrospectiveAndPlanning;

@Repository
public interface SprintRetrospectiveAndPlanningDAO extends MongoRepository<SprintRetrospectiveAndPlanning, String> {

	public void deleteByIdIn(List<String> ids);

	public List<SprintRetrospectiveAndPlanning> findBySprintNameOrderByUserId(String sprintName);

	public List<SprintRetrospectiveAndPlanning> findByProjectIdOrderByUserId(String projectId);
	
	public SprintRetrospectiveAndPlanning findByUserId(String userId);

}
