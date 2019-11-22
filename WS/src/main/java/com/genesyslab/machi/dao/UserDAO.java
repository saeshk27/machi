package com.genesyslab.machi.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.User;

@Repository
public interface UserDAO extends MongoRepository<User, String> {

	public User findByLoginIdIgnoreCaseAndPassword(String loginId, String password);

	public User findByEmailIgnoreCase(String email);

	public void deleteByIdIn(List<String> ids);

	public List<User> findByReportingId(String reportingId);

	public List<User> findByProjectId(String projectId);

	public List<User> findByIdIn(List<String> ids);
}
