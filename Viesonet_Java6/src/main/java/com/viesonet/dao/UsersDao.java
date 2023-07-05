package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

public interface UsersDao extends JpaRepository<Users, String> {
	@Query("SELECT b FROM Users b WHERE b.userId=?1")
	List<Posts> findUserByUserId(String userId);
}
