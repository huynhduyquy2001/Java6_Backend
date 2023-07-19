package com.viesonet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.AccountsDao;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Users;
@Service
public class AccountsService {
	@Autowired
	AccountsDao accountsDao;

	public Accounts getAccountByUsers(String userId) {
        return accountsDao.findByUserId(userId);
    }
	
	public Accounts findByPhoneNumber(String phoneNumber) {
		return accountsDao.findByphoneNumber(phoneNumber);
	}
	
}
