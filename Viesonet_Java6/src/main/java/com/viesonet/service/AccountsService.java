package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountsDao;
import com.viesonet.entity.Accounts;

@Service
public class AccountsService {
	@Autowired 
	AccountsDao aDao;
	
	public Accounts findByPhoneNumber(String phoneNumber) {
		return aDao.findByPhoneNumber(phoneNumber);
	}
}
