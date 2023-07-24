package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Posts;

public interface PostsDao extends JpaRepository<Posts, Integer>{
	@Query("SELECT b FROM Posts b WHERE b.user.userId IN :userId AND b.isActive=true")
	List<Posts> findPostsByListUserId(List<String> userId, Sort sort);
	
	@Query("SELECT b FROM Posts b WHERE b.user.userId = ?1 AND b.isActive=true")
	List<Posts> getMyPosts(String userId);
	
	@Query("SELECT COUNT(b) FROM Posts b WHERE b.user.userId = :userId")
    Integer countMyPosts(String userId);
	
	@Query("SELECT p FROM Posts p JOIN p.images i WHERE p.user.userId = :userId AND p.isActive=true")
    List<Posts> findPostsByUserId(String userId);
}
	