package com.viesonet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountsDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;
import com.viesonet.entity.Users;
@Service
public class AccountsService {
	@Autowired
	AccountsDao accountsDao;

	public Accounts getAccountByUsers(String userId) {
        return accountsDao.findByUserId(userId);
    }
	
	public Accounts findByPhoneNumber(String phoneNumber) {
		return accountsDao.findByPhoneNumber(phoneNumber);
	}
	
	public boolean existById(String phoneNumber) {
		return accountsDao.existsById(phoneNumber);
	}
	
	public boolean existByEmail(String email) {
		return accountsDao.existsByEmail(email);
	}
	
	public Accounts save(Accounts accounts) {
		return accountsDao.save(accounts);
	}

	public Accounts setRole(String sdt, int role) {
		Roles roles = new Roles();
		roles.setRoleId(role);
		Accounts accounts = accountsDao.findByPhoneNumber(sdt);
		accounts.setRole(roles);
		return accountsDao.saveAndFlush(accounts);
	}
	
}
