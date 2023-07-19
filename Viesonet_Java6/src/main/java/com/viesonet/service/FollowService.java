package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FollowDao;
import com.viesonet.entity.Follow;

@Service
public class FollowService {

	@Autowired
	FollowDao followDao;
	public List<Follow> getFollowing(String followingId){
		return followDao.findByFollowingId(followingId);
	}
}
