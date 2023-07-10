package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Posts;

public interface PostsDao extends JpaRepository<Posts, Integer>{
}
