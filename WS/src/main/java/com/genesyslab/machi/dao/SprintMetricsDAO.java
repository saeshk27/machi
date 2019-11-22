package com.genesyslab.machi.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.SprintMetrics;

@Repository
public interface SprintMetricsDAO extends MongoRepository<SprintMetrics, String> {

	public void deleteByIdIn(List<String> ids);

	public List<SprintMetrics> findByProjectIdOrderBySprintNameAscUserIdAsc(String projectId);
	
	public List<SprintMetrics> findBySprintNameOrderByUserId(String sprintName);
	
	public List<SprintMetrics> findByUserId(String userId);

}
