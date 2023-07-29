package com.viesonet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationsDao;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;
import com.viesonet.entity.ViolationTypes;
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
	
	public void deleteByPostViolations(List<String> listPostId) {
		for (String id : listPostId) {
	           List<Violations> violations = violationsDAO.findByPostId(Integer.parseInt(id));
	           violationsDAO.deleteAll(violations);
		}
	}
	public Violations report(Users user, Posts post, ViolationTypes violationType) {
		Violations obj = new Violations();
		obj.setPost(post);
		obj.setReportDate(new Date());
		obj.setUser(user);
		obj.setViolationType(violationType);
		obj.setViolationStatus(false);
		violationsDAO.saveAndFlush(obj);
		return obj;
	}
}
