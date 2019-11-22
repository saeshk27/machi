package com.genesyslab.machi.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.DBHealthCheck;

@Repository
public interface DBHealthCheckDAO extends MongoRepository<DBHealthCheck, String> {

	public DBHealthCheck findFirstByHealthy(boolean healthy);

}
