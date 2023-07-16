package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationsDao;
import com.viesonet.entity.Violations;

@Service
public class ViolationsService {
	@Autowired
	ViolationsDao violationsDAO;
	
	public List<Object> findList(int postId){
		return violationsDAO.findList(postId);
	}
	
	public Page<Object> findAllListFalse(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return violationsDAO.findAllListFalse(pageable);
	}
	
	public List<Violations> findByPostId(int postId){
		return violationsDAO.findByPostId(postId);
	}
}
