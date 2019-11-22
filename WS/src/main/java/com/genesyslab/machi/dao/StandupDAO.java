package com.genesyslab.machi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.genesyslab.machi.domain.Standup;

@Repository
public interface StandupDAO extends MongoRepository<Standup, String> {

	public void deleteByIdIn(List<String> ids);

	public List<Standup> findByProjectIdAndMeetingDateOrderByUserIdAscTypeDesc(String projectId, Date meetingDate);

	public List<Standup> findByProjectIdInOrderByMeetingDateDescUserIdAscTypeAsc(String projectId);

	public Standup findTopByProjectIdInOrderByMeetingDateDesc(String projectId);

	public List<Standup> findByUserIdInOrderByMeetingDateDescTypeAsc(String userId);

	public List<Standup> findTop3ByUserIdInOrderByMeetingDateDescTypeAsc(String userId);

}
