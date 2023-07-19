package com.viesonet.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viesonet.dao.AccountsDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;
import com.viesonet.entity.Users;

import jakarta.websocket.server.PathParam;

@Controller
public class UsermanagerController {

	@Autowired
	UsersDao userDAO;
	
	@Autowired
	AccountsDao accDAO;
	
	@GetMapping("/admin/usermanager")
	public String usermanager(Model m) {
		// Tìm người dùng vai trò là admin
		m.addAttribute("acc", userDAO.findByuserId("UI001"));
		
		//Lấy danh sách người dùng
		m.addAttribute("listUser", userDAO.findByUserAndStaff("UI001"));
		return "/admin/usermanager";
	}
	
	//Phương thức tìm kiếm người dùng
	@ResponseBody
	@RequestMapping("/admin/usermanager/search/{key}")
	public List<Object> search(@PathVariable String key) {
		//Tạo list để chứa
		List<Object> searchUser;
		//Xét điều kiện để tìm
		if(key.equals("all")) {
			//Tìm hết
			 searchUser = userDAO.findByUserSearchAll();
		}else {
			//Tìm theo chữ đã nhập
			 searchUser = userDAO.findByUserSearch(key);
		}
		return searchUser;
	}
	
	//Phương thức lấy thông tin chi tiết người dùng
	@ResponseBody
	@RequestMapping("/admin/usermanager/detailUser/{userId}")
	public Object detailUser(@PathVariable String userId) {
		//Tìm thông tin chi tiết người dùng
		Object searchUser = userDAO.findByDetailUser(userId);
		return searchUser;
	}
	
	//Phương thức đổi vai trò người dùng
		@ResponseBody
		@RequestMapping("/admin/usermanager/userRole/{role}/{userId}/{sdt}")
		public Object userRole(@PathVariable int role, @PathVariable String userId, @PathVariable String sdt) {
			//Đổi vai trò tài khoản 
			Accounts account = new Accounts();
			Roles roles = new Roles();
			roles.setRoleId(role);
			account = accDAO.findByphoneNumber(sdt);
			if(account.getRole().getRoleId() == role) {
				return "warning";
			}
			account.setRole(roles);
			accDAO.saveAndFlush(account);
			//Tìm thông tin người dùng này
			Object searchUser = userDAO.findByDetailUser(userId);
			return searchUser;
		}
		
		
		//Phương thức gỡ vi phạm người dùng
				@ResponseBody
				@RequestMapping("/admin/usermanager/userViolations/{userId}")
				public Object userViolations(@PathVariable String userId) {
					//Gỡ vi phạm cho người dùng
					Users user = new Users();
					user = userDAO.findByuserId(userId);
					if(user.getViolationCount() == 0) {
						return "warning";
					}
					user.setViolationCount(0);
					userDAO.saveAndFlush(user);
					//Tìm thông tin người dùng này
					Object searchUser = userDAO.findByDetailUser(userId);
					return searchUser;
				}
}
