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
	
	public List<Object> findSearchUserViolation(String username){
		return violationsDAO.findSearchUserViolation(username);
	}
	
	public List<Object> findSearchUserViolations(String username){
		return violationsDAO.findSearchUserViolations(username);
	}
	
	public void deleteByPostViolations(List<String> listPostId) {
		for (String id : listPostId) {
	           List<Violations> violations = violationsDAO.findByPostId(Integer.parseInt(id));
	           violationsDAO.deleteAll(violations);
		}
	}
	
	public Page<Object> findViolationsWithStatusTrue(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return violationsDAO.findViolationsWithStatusTrue(pageable);
	}
	
	public void acceptByPostViolations(List<String> listPostId) {
	    for (String id : listPostId) {
	        int postId = Integer.parseInt(id);
	        List<Violations> violations = violationsDAO.findByPostId(postId);
	        for (Violations violation : violations) {
	            violation.setViolationStatus(false);
	            violationsDAO.saveAllAndFlush(violations);
	        }
	        
	    }
	}

	
}
