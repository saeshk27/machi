package com.genesyslab.machi.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.Project;

@Repository
public interface ProjectDAO extends MongoRepository<Project, String> {

	public void deleteByIdIn(List<String> ids);

	public List<Project> findByProductOwnerId(String productOwnerId);

}
