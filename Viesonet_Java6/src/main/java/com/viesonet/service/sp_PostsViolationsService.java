package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.entity.PostsViolationsAndUser;
import com.viesonet.report.sp_PostsViolations;

@Service
public class sp_PostsViolationsService {
	@Autowired
	sp_PostsViolations sp;
	
	public List<PostsViolationsAndUser> postsViolationsAndUsers(){
		List<PostsViolationsAndUser> list = sp.executePostsViolations();
		return list;
	}
}
