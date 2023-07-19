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
}
