package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;

public interface ImagesDao extends JpaRepository<Images, Integer> {
	
	

}
