package com.viesonet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Roles;
import com.viesonet.entity.UserInformation;
import com.viesonet.entity.Users;
import com.viesonet.entity.AccountStatus;
@Service
	public class UsersService {
	
	@Autowired
	UsersDao usersDao;
	
	public Users findUserById(String userId) {
		Optional<Users> user = usersDao.findById(userId);
		return user.orElse(null);
	}
	
	//Phương thức lấy thông tin chi tiết người dùng
	public UserInformation getMyInfomation(Users user, Accounts account) {
	    UserInformation obj = new UserInformation();
	    obj.setUser(user);
	    
	    
	    obj.setIntrodution(user.getIntroduction());
	    obj.setBirthday(user.getBirthday());
	    obj.setGender(user.isGender());
	    obj.setRelationship(user.getRelationship());

	    return obj;
	}


}
