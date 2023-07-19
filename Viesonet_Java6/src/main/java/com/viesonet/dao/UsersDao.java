package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

public interface UsersDao extends JpaRepository<Users, String> {
	@Query("SELECT b FROM Users b WHERE b.userId=?1")
	List<Posts> findUserByUserId(String userId);
	
	@Query("SELECT u.userId, u.avatar, u.username, u.account.email, u.violationCount FROM Users u WHERE u.username LIKE %:userName%")
	List<Users> findByUserSearch(@Param("userName") String userName);
	
	@Query("SELECT b FROM Users b WHERE b.userId=?1")
	List<Users> findByUser(String userId);
	
	List<Users> findByUsernameContaining(String username);
}
