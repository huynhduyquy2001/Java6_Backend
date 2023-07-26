package com.viesonet.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Accounts;
import com.viesonet.service.AccountsService;
import com.viesonet.service.SessionService;

@RestController
public class ChangePasswordController2 {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private AccountsService accountsService;

	@GetMapping("/change_password")
	public ModelAndView getChangePasswordPage() {
		ModelAndView modelAndView = new ModelAndView("ChangePassword2");
		return modelAndView;
	}

	@PostMapping("/doimatkhau2")
	public ResponseEntity<?> doimatkhau2(@RequestBody Map<String, Object> data) {
		String matKhauMoi = (String) data.get("matKhauMoi");
		String matKhauXacNhan = (String) data.get("matKhauXacNhan");
		String sdt = sessionService.get("phone");
		Accounts accounts = accountsService.findByPhoneNumber(sdt);

		if (matKhauMoi.equalsIgnoreCase(accounts.getPassword())) {
			return ResponseEntity.ok()
					.body(Collections.singletonMap("message", "Mật khẩu mới không được giống mật khẩu cũ"));
		} else {
			if (matKhauMoi.equalsIgnoreCase(matKhauXacNhan)) {
				accounts.setPassword(matKhauMoi);
				accountsService.save(accounts);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.ok()
						.body(Collections.singletonMap("message", "Mật khẩu và mật khẩu xác nhận không khớp"));
			}
		}
	}

}
