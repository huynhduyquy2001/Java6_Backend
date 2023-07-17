package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FollowDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Users;

@Service
public class FollowService {

	@Autowired
	FollowDao followDao;
	public List<Follow> getFollowing(String followingId){
		return followDao.findByFollowingId(followingId);
	}
	public AccountAndFollow getFollowingFollower(Users user){
		AccountAndFollow obj = new AccountAndFollow();
		obj.setUser(user);
		obj.setFollowers(followDao.getFollowersById(user));
		obj.setFollowing(followDao.getFollowingById(user));
		return obj;
		
	}
	
	
}
