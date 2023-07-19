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
import com.viesonet.service.AccountsService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.websocket.server.PathParam;

@Controller
public class UsermanagerController {

	@Autowired
	UsersService userService;
	
	@Autowired
	AccountsService accountService;
	
	@Autowired
	private SessionService sessionService;
	
	@GetMapping("/admin/usermanager")
	public String usermanager(Model m) {
		// Tìm người dùng vai trò là admin
		String userId = sessionService.get("id");
		m.addAttribute("acc", userService.findUserById(userId));
		
		//Lấy danh sách người dùng
		m.addAttribute("listUser", userService.findByUserAndStaff(userId));
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
			 searchUser = userService.findByUserSearchAll();
		}else {
			//Tìm theo chữ đã nhập
			 searchUser = userService.findByUserSearch(key);
		}
		return searchUser;
	}
	
	//Phương thức lấy thông tin chi tiết người dùng
	@ResponseBody
	@RequestMapping("/admin/usermanager/detailUser/{userId}")
	public Object detailUser(@PathVariable String userId) {
		//Tìm thông tin chi tiết người dùng
		Object searchUser = userService.findByDetailUser(userId);
		return searchUser;
	}
	
	//Phương thức đổi vai trò người dùng
		@ResponseBody
		@RequestMapping("/admin/usermanager/userRole/{role}/{userId}/{sdt}")
		public Object userRole(@PathVariable int role, @PathVariable String userId, @PathVariable String sdt) {
			//Đổi vai trò tài khoản 
			Accounts account = new Accounts();
			account = accountService.findByphoneNumber(sdt);
			if(account.getRole().getRoleId() == role) {
				return "warning";
			}
			accountService.setRole(sdt, role);
			//Tìm thông tin người dùng này
			Object searchUser = userService.findByDetailUser(userId);
			return searchUser;
		}
		
		
		//Phương thức gỡ vi phạm người dùng
				@ResponseBody
				@RequestMapping("/admin/usermanager/userViolations/{userId}")
				public Object userViolations(@PathVariable String userId) {
					//Gỡ vi phạm cho người dùng
					Users user = new Users();
					user = userService.findUserById(userId);
					if(user.getViolationCount() == 0) {
						return "warning";
					}
					
					userService.setViolationCount(userId);
					//Tìm thông tin người dùng này
					Object searchUser = userService.findByDetailUser(userId);
					return searchUser;
				}
}
