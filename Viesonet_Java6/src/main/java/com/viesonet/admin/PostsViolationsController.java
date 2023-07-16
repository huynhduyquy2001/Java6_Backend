package com.viesonet.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.viesonet.entity.*;
import com.viesonet.report.sp_PostsViolations;
import com.viesonet.service.UsersService;
import com.viesonet.service.ViolationsService;
import com.viesonet.service.sp_PostsViolationsService;

import jakarta.transaction.Transactional;

import com.viesonet.dao.UsersDao;
import com.viesonet.dao.ViolationsDao;


@Controller
public class PostsViolationsController {

	@Autowired
	UsersService userService;
	
	@Autowired
	ViolationsService violationsService;
	
	@Autowired
	sp_PostsViolationsService spService;
	
	@GetMapping("/admin/postsviolations")
	public String postsViolations(Model m,  @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
		// Tìm người dùng vai trò là admin
		m.addAttribute("acc", userService.findUserById("UI001"));
		
		// Lấy danh sách bài viết vi phạm
		m.addAttribute("listPosts", violationsService.findAllListFalse(page, size));
		return "admin/postsviolations";
	}
	
//	@ResponseBody
//	@RequestMapping("/admin/postsviolations/list/{id}")
//	public List<Object> list(@PathVariable int id) {
//		//Lấy danh sách lý do tố cáo
//		return violationsDAO.findList(id);
//	}
//	
//	@ResponseBody
//	@RequestMapping("/admin/postsviolations/removeViolations/{id}")
//	@Transactional
//	public List<PostsViolationsAndUser> removeViolations(@PathVariable int id) {
//
//		List<Violations> entity = violationsDAO.findByPostId(id);
//		violationsDAO.deleteAll(entity);
//																			
//		//Lấy lại danh sách 
//		List<PostsViolationsAndUser> list = spDAO.executePostsViolations();
//		return list;
//	}
}
