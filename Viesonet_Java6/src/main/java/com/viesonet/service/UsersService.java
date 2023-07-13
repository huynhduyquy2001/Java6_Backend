package com.viesonet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	
	public List<Users> findByUserAndStaff(String userId){
		List<Users> list = usersDao.findByUserAndStaff(userId);
		return list;
	}
	
	public List<Object> findByUserSearch(String userId){
		List<Object> list = usersDao.findByUserSearch(userId);
		return list;
	}
	
	public List<Object> findByUserSearchAll(){
		List<Object> list = usersDao.findByUserSearchAll();
		return list;
	}
	
	public Object findByDetailUser(String userId) {
		Object detailUser = usersDao.findByDetailUser(userId);
		return detailUser;
	}
	
}
