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

	public Accounts findAccountPhoneNumber(String sdt) {
		Optional<Accounts> acc =  accountsDao.findByPhoneNumber(sdt);
		return acc.orElse(null);
	}
	
}
