package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.viesonet.entity.Users;
import com.viesonet.entity.WebSocKetMessage;
import com.viesonet.service.*;


@RestController
public class MessageController {
	
	@Autowired
	UsersService userService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
    MessageTemplateService msService;
	
	@GetMapping("/chat/profile")
	public Users profile() {
		String userId = sessionService.get("id");
		return userService.findUserById(userId);
	}
	
	@GetMapping("/chat/chatuser/{userId}")
	public Users chatUser(@PathVariable String userId) {
		return userService.findUserById(userId);
	}
	
	@GetMapping("/chat/list")
	public List<Users> list() {
		String userId = sessionService.get("id");
		return userService.findByUserAndStaff(userId);
	}
	
	@MessageMapping("/chat.register")
	@SendTo("/topic/public")
	public WebSocKetMessage register(@Payload WebSocKetMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("sender", chatMessage.getSender());
		return chatMessage;
	}

	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public WebSocKetMessage sendMessage(@Payload WebSocKetMessage chatMessage) {
		return chatMessage;
	}
	
	@MessageMapping("/resume.{username}")
    @SendTo("/topic/initial.{username}")
    public void chat(String msg, @DestinationVariable String username, SimpMessageHeaderAccessor headerAccessor) {
        msService.convertAndSend("/start/initial."+username,msg);
    }

    @MessageMapping("/adduser.{username}")
    @SendTo("/topic/initial.{username}")
    public void addUser(String chatMessage, @DestinationVariable String username, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username",username);
        msService.convertAndSend("/start/initial."+username,chatMessage);
    }
	
	@GetMapping("/message")
	public ModelAndView logout() {
		return new ModelAndView("chatbox");
	}
}
