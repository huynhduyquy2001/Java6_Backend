package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountsDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;

@Service
public class AccountsService {
	@Autowired 
	AccountsDao aDao;
	
	public Accounts findByPhoneNumber(String phoneNumber) {
		return aDao.findByPhoneNumber(phoneNumber);
	}
	
	public boolean existById(String phoneNumber) {
		return aDao.existsById(phoneNumber);
	}
	
	public boolean existByEmail(String email) {
		return aDao.existsByEmail(email);
	}
	
	public Accounts save(Accounts accounts) {
		return aDao.save(accounts);
	}
	
}
