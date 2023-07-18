package com.viesonet.service;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Users;

@Service
public class UsersService {
	@Autowired
	UsersDao usersDao;

	public Users findUserById(String userId) {
		Optional<Users> user = usersDao.findById(userId);
		return user.orElse(null);
	}

	public Users save(Users users) {
		return usersDao.save(users);
	}

	public Users getById(String userId) {
		return usersDao.getById(userId);
	}

	public List<Users> findByUserAndStaff(String userId) {
		return usersDao.findByUserAndStaff(userId);
	}

	public List<Object> findByUserSearch(String userId) {
		return usersDao.findByUserSearch(userId);
	}
	
	public List<Object> findByUserSearchAll(){
		return usersDao.findByUserSearchAll();
	}
	
	public Object findByDetailUser(String userId) {
		return usersDao.findByDetailUser(userId);
	}
	
	public List<Users> findAll(){
		return usersDao.findAll();
	}
	
	public Users setViolationCount(String userId) {
		Users user = usersDao.findByuserId(userId);
		user.setViolationCount(0);
		return usersDao.saveAndFlush(user);
	}
}
