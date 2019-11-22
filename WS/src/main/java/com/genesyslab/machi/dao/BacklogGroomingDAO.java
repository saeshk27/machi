package com.genesyslab.machi.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.BacklogGrooming;

@Repository
public interface BacklogGroomingDAO extends MongoRepository<BacklogGrooming, String> {

	public void deleteByIdIn(List<String> ids);

	public List<BacklogGrooming> findBySprintIdOrderByTicketNo(String sprintId);

	public List<BacklogGrooming> findByProjectIdOrderByTicketNo(String projectId);

}
