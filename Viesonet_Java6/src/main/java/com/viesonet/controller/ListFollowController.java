package com.viesonet.controller;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.service.FollowService;
import com.viesonet.service.SearchService;
import com.viesonet.service.SessionService;

import jakarta.websocket.server.PathParam;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Users;

@RestController
public class ListFollowController {
	@Autowired
	private SearchService SearchService;
	@Autowired
	private UsersDao UsersDao;
	@Autowired
	private FollowService followService;
	@Autowired
	private SessionService session;

	@GetMapping("/ListFollow")
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("ListFollow");
		return modelAndView;
	}

	// Lấy thông tin chi tiết các followers
	@GetMapping("/ListFollower")
	public List<Users> getFollowersInfoByUserId(@SessionAttribute("id") String userId) {
		return followService.getFollowersInfoByUserId(session.get("id"));
	}

	// Lấy thông tin chi tiết các followers
	@GetMapping("/ListFollowing")
	public List<Users> getFollowingInfoByUserId(@SessionAttribute("id") String userId) {
		return followService.getFollowingInfoByUserId(session.get("id"));
	}
}
