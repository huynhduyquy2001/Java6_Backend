package com.viesonet.service;

import java.util.ArrayList;
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

	public List<Follow> getFollowing(String followingId) {
		return followDao.findByFollowingId(followingId);
	}

	public AccountAndFollow getFollowingFollower(Users user) {
		AccountAndFollow obj = new AccountAndFollow();
		obj.setUser(user);
		obj.setFollowers(followDao.getFollowersById(user));
		obj.setFollowing(followDao.getFollowingById(user));
		return obj;

	}

	public List<Users> getFollowersInfoByUserId(String userId) {
		return followDao.findFollowersInfoByUserId(userId);
	}

	public List<Follow> getAllFollowers() {
		return followDao.findAll();
	}

	public List<Users> getFollowingInfoByUserId(String userId) {
		return followDao.findFollowingInfoByUserId(userId);
	}

	public List<String> findUserIdsOfFollowing(String userId) {
		return followDao.findUserIdsOfFollowing(userId);
	}
	
	 public void followUser(String followerId, String followingId) {
	        Follow follow = new Follow();
	        Users follower = new Users();
	        follower.setUserId(followerId);
	        Users following = new Users();
	        following.setUserId(followingId);

	        follow.setFollower(follower);
	        follow.setFollowing(following);

	        followDao.save(follow);
	    }

	    public void unfollowUser(String followerId, String followingId) {
	    	followDao.deleteByFollowerUserIdAndFollowingUserId(followerId, followingId);
	    }
}
