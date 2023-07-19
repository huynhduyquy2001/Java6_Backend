package com.viesonet.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Accounts;
import com.viesonet.service.AccountsService;
import com.viesonet.service.CookieService;
import com.viesonet.service.SessionService;

@RestController
public class LoginController {
	@Autowired
	private AccountsService accountsService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private CookieService cookieService;

	@GetMapping("/login")
	public ModelAndView getLoginPage() {
		String user = cookieService.getValue("user");
		
		if (user != null) {
			Accounts accounts = accountsService.findByPhoneNumber(user);
			sessionService.set("role", accounts.getRole().getRoleId());
			sessionService.set("id", accounts.getUser().getUserId());
			sessionService.set("phone", accounts.getPhoneNumber());
			return new ModelAndView("redirect:/");
		} else if (sessionService.get("id") != null) {
			return new ModelAndView("redirect:/");
		} else {
			return new ModelAndView("Login");
		}
	}
		

	@PostMapping("/dangnhap")
	public ResponseEntity<?> dangNhap2(@RequestBody Map<String, Object> data) {
		String sdt = (String) data.get("sdt");
		String matKhau = (String) data.get("matKhau");
		boolean remember = data.get("ghiNho") == null ? false : (Boolean) data.get("ghiNho");
		Accounts accounts = accountsService.findByPhoneNumber(sdt);
		if (accounts == null) {
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Số điện thoại không tồn tại"));
		}
		if (sdt.equals(accounts.getPhoneNumber()) && matKhau.equals(accounts.getPassword())) {
			if (accounts.getAccountStatus().getStatusId() == 4) {
				return ResponseEntity.ok()
						.body(Collections.singletonMap("message", "Tài khoản này đã bị khóa do vi phạm điều khoản"));
			} else {
				sessionService.set("role", accounts.getRole().getRoleId());
				sessionService.set("id", accounts.getUser().getUserId());
				sessionService.set("phone", accounts.getPhoneNumber());

				if (remember) {
					cookieService.add("user", sdt, 10);
					cookieService.add("pass", matKhau, 10);
				} else {
					cookieService.delete("user");
					cookieService.delete("pass");
				}
				return ResponseEntity.ok().build();
			}
		} else {
			return ResponseEntity.ok()
					.body(Collections.singletonMap("message", "Thông tin đăng nhập không chính xác !"));
		}
	}

}
