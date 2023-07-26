package com.viesonet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

	public String generateRandomString() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 4; i++) {
			int index = random.nextInt(characters.length());
			char randomChar = characters.charAt(index);
			sb.append(randomChar);
		}
		return sb.toString();
	}

	public String generateRandomNumbers() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int randomNumber = random.nextInt(10); // Sinh số ngẫu nhiên từ 0 đến 9
			sb.append(randomNumber);
		}
		return sb.toString();
	}

	String numbers = generateRandomNumbers();
	String randomString = generateRandomString();
	String id = randomString + numbers;

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
		if (usersService.existById(id)) {
			String newRandomString = generateRandomString();
			String newRandomNumbers = generateRandomNumbers();
			id = newRandomString + newRandomNumbers;
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
						account.setPassword(password);
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
