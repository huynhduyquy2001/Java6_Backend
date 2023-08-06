package com.viesonet.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.service.AccountsService;
import com.viesonet.service.SessionService;

@RestController
public class ChangePasswordController {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	AuthConfig authConfig; 
	

	
	@GetMapping("/changepassword")
	public ModelAndView getChangePasswordPage() {
		ModelAndView modelAndView = new ModelAndView("ChangePassword");
		return modelAndView;
	}

	@PostMapping("/doimatkhau")
	public ResponseEntity<?> doimatkhau(@RequestBody Map<String, Object> data, Authentication authentication) {
	    String matKhau = (String) data.get("matKhau");
	    String matKhauMoi = (String) data.get("matKhauMoi");
	    String matKhauXacNhan = (String) data.get("matKhauXacNhan");

	    
	    
	    
	    Accounts accounts = authConfig.getLoggedInAccount(authentication);
	    System.out.println("Mật khẩu cũ trong sql: "+ accounts.getPassword());
	    
	    String hashedPassword = authConfig.passwordEncoder().encode(matKhau);
	    System.out.println("Mật khẩu mã hóa trong input: "+ hashedPassword);
	    PasswordEncoder passwordEncoder = authConfig.passwordEncoder();
	    
	    if (passwordEncoder.matches(matKhau, accounts.getPassword())) {
	        String hashedNewPassword = passwordEncoder.encode(matKhauMoi);
	        // Kiểm tra tính khớp giữa mật khẩu mới và mật khẩu xác nhận
	        if (matKhauMoi.equals(matKhauXacNhan)) {
	            accounts.setPassword(hashedNewPassword);
	            accountsService.save(accounts);
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
	        }
	    } else {
	        return ResponseEntity.ok().body(Collections.singletonMap("message", "Mật khẩu cũ không trùng khớp"));
	    }
	}

}