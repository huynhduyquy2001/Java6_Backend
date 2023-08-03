package com.viesonet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountStatusService;
import com.viesonet.service.AccountsService;
import com.viesonet.service.RolesService;
import com.viesonet.service.UsersService;

@RestController
public class RegisterController {
	@Autowired
	AccountsService accountsService;

	@Autowired
	UsersService usersService;

	@Autowired
	RolesService rolesService;

	@Autowired
	AccountStatusService accountStatusService;
	
	@Autowired
	AuthConfig authConfig;

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
	String dateStr = now.format(dateFormatter);
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
	String timeStr = now.format(timeFormatter);
	String id = dateStr + timeStr;

	@GetMapping("/register")
	public ModelAndView getRegisterPage() {
		ModelAndView modelAndView = new ModelAndView("Register");
		return modelAndView;
	}

	@PostMapping("/dangky")
	public ResponseEntity<?> dangKy2(@RequestBody Map<String, Object> data) {
		
		String phoneNumber = (String) data.get("phoneNumber");
		String password = (String) data.get("password");
		String confirmPassword = (String) data.get("confirmPassword");
		String username = (String) data.get("username");
		String email = (String) data.get("email");
		boolean gender = Boolean.parseBoolean(data.get("gender").toString());
		boolean accept = false;
		
		if (data.containsKey("accept")) {
			accept = Boolean.parseBoolean(data.get("accept").toString());
		}

		if (!accept) {
			return ResponseEntity.ok()
					.body(Collections.singletonMap("message", "Phải chấp nhận điều khoản để đăng kí tài khoản"));
		} else {
			if (!accountsService.existById(phoneNumber)) {
				if (accountsService.existByEmail(email)) {
					return ResponseEntity.ok().body(Collections.singletonMap("message", "Email này đã được đăng ký"));
				} else {
					if (password.equalsIgnoreCase(confirmPassword)) {
						String hashedPassword = authConfig.passwordEncoder().encode(password);
						Users user = new Users();
						user.setAvatar(gender ? "avatar1.jpg" : "avatar2.jpg");
						user.setViolationCount(0);
						user.setBackground("nen.jpg");
						user.setCreateDate(new Date());
						user.setUserId(id);
						user.setUsername(username);
						usersService.save(user);

						Accounts account = new Accounts();
						account.setPhoneNumber(phoneNumber);
						account.setPassword(hashedPassword);
						account.setEmail(email);
						account.setUser(usersService.getById(id));
						account.setRole(rolesService.getById(3));
						account.setAccountStatus(accountStatusService.getById(1));
						accountsService.save(account);
					} else {
						return ResponseEntity.ok().body(
								Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không trùng khớp"));
					}
					return ResponseEntity.ok().build();
				}
			} else {
				return ResponseEntity.ok().body(Collections.singletonMap("message", "Tài khoản đã tồn tại"));
			}
		}
	}
}
