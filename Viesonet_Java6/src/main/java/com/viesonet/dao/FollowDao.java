package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Follow;


public interface FollowDao extends JpaRepository<Follow, Integer> {
	@Query("SELECT obj from Follow obj where obj.follower.userId = ?1")
    List<Follow> findByFollowingId(String followingId);
}
