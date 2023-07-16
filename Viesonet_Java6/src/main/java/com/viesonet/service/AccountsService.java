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
		return accountDAO.findByphoneNumber(phoneNumber);
	}
	
	public Accounts setRole(String sdt, int role) {
		Roles roles = new Roles();
		roles.setRoleId(role);
		Accounts accounts = accountDAO.findByphoneNumber(sdt);
		accounts.setRole(roles);
		return accountDAO.saveAndFlush(accounts);
	}
}
