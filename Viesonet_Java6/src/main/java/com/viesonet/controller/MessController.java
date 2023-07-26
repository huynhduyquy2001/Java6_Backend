package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;
import com.viesonet.entity.Users;
import com.viesonet.service.MessageService;
import com.viesonet.service.UsersService;

@RestController
public class MessController {
	
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	UsersService usersService;
	
@GetMapping("/mess")
public ModelAndView getHomePage() {
    ModelAndView modelAndView = new ModelAndView("Message");
    return modelAndView;
}


@GetMapping("/getmess")
public List<Message> getListMess(){
	return messageService.getListMess("UI011", "UI010");
}

@GetMapping("/getmess2/{userId}")
public List<Message> getListMess2(@PathVariable("userId") String userId){
	return messageService.getListMess("UI011", userId);
}


@GetMapping("/getusersmess")
public List<Object> getUsersMess(){
	return messageService.getListUsersMess("UI011");
}

@PostMapping("/getUser/{userId}")
public Users findUserById(@PathVariable("userId") String userId) {
	return usersService.findUserById(userId);
}
}
