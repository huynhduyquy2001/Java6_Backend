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

}
