package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountsDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;

@Service
public class AccountsService {
	@Autowired
	AccountsDao accountDAO;
	
	public Accounts findByphoneNumber(String phoneNumber) {
		return accountDAO.findByPhoneNumber(phoneNumber);
	}
	
	
	public Accounts setRole(String sdt, int role) {
		Roles roles = new Roles();
		roles.setRoleId(role);
		Accounts accounts = accountDAO.findByPhoneNumber(sdt);
		accounts.setRole(roles);
		return accountDAO.saveAndFlush(accounts);
	}

	public Accounts findByPhoneNumber(String phoneNumber) {
		return accountDAO.findByPhoneNumber(phoneNumber);
	}
	
	public Accounts findByEmail(String email) {
		return accountDAO.findByEmail(email);
	}
	
	public boolean existById(String phoneNumber) {
		return accountDAO.existsById(phoneNumber);
	}
	
	public boolean existByEmail(String email) {
		return accountDAO.existsByEmail(email);
	}
	
	public Accounts save(Accounts accounts) {
		return accountDAO.save(accounts);
	}
	
}
