package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;
import com.viesonet.service.MessageService;

@RestController
public class MessController {
	
	
	@Autowired
	MessageService messageService;
	
@GetMapping("/mess")
public ModelAndView getHomePage() {
    ModelAndView modelAndView = new ModelAndView("Message");
    return modelAndView;
}


@GetMapping("/getmess")
public List<Message> getListMess(){
	return messageService.getListMess("UI011", "UI010");
}


@GetMapping("/getusersmess")
public List<Object> getUsersMess(){
	return messageService.getListUsersMess("UI011");
}
}
