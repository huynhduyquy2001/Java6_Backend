package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Follow;
import com.viesonet.entity.Users;


public interface FollowDao extends JpaRepository<Follow, Integer> {
	@Query("SELECT obj from Follow obj where obj.follower.userId = ?1")
    List<Follow> findByFollowingId(String followingId);
	
	@Query("SELECT COUNT(obj)  from Follow obj where obj.following = ?1")
    int getFollowersById(Users user);
	
	@Query("SELECT COUNT(obj) from Follow obj where obj.follower = ?1")
    int getFollowingById(Users user);
	
}
