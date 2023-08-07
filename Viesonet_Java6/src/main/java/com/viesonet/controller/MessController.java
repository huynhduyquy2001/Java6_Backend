package com.viesonet.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.viesonet.AuthConfig;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Message;
import com.viesonet.entity.MessageRequest;
import com.viesonet.entity.UserMessage;
import com.viesonet.entity.Users;
import com.viesonet.service.MessageService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

@RestController
public class MessController {

	@Autowired
	MessageService messageService;

	@Autowired
	UsersService usersService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private AuthConfig authConfig;

//	@GetMapping("/mess")
//	public ModelAndView getHomePage() {
//		ModelAndView modelAndView = new ModelAndView("Message");
//		return modelAndView;
//	}
	

	@MessageMapping("/sendnewmess")
	@SendToUser("/queue/receiveMessage")
	public void addMess(Message newMessage) {
		messagingTemplate.convertAndSendToUser(newMessage.getReceiver().getUserId(), "/queue/receiveMessage",
				newMessage);
	}
	

	@PostMapping("/savemess")
	public Message saveMess(@RequestBody MessageRequest messageRequest) {
		// Thêm tin nhắn vào cơ sở dữ liệu
		Message newMessage = messageService.addMess(usersService.findUserById(messageRequest.getSenderId()),
				usersService.findUserById(messageRequest.getReceiverId()), messageRequest.getContent());
		return newMessage;
	}

	@GetMapping("/getmess2/{userId}")
	public List<Message> getListMess2(@PathVariable("userId") String userId, Authentication authentication) {
		String myId = authConfig.getLoggedInAccount(authentication).getUserId();
		return messageService.getListMess(myId, userId);
	}

	@GetMapping("/getusersmess")
	public List<Object> getUsersMess( Authentication authentication) {
		String myId = authConfig.getLoggedInAccount(authentication).getUserId();
		return messageService.getListUsersMess(myId);
	}

	@GetMapping("/getUser/{userId}")
	public Users findUserById(@PathVariable("userId") String userId, Model model) {
		return usersService.findUserById(userId);
	}

	@GetMapping("/getunseenmessage")
	public int getListUnseenMessage(Authentication authentication) {
		String myId = authConfig.getLoggedInAccount(authentication).getUserId();
		return messageService.getListUnseenMessage(myId);
	}

	@PostMapping("/seen/{userId}")
	public List<Message> seen(@PathVariable("userId") String senderId, Authentication authentication) {
		String myId = authConfig.getLoggedInAccount(authentication).getUserId();
		return messageService.seen(senderId, myId);
	}
	@PostMapping("/removemess/{messId}")
	public Message reMoveMess(@PathVariable("messId") int messId) {
		return messageService.removeMess(messageService.getMessById(messId));
	}
	
	@RequestMapping(value = "/mess/{otherId}", method = RequestMethod.GET)
	public ModelAndView loadUserPage(@PathVariable("otherId") String id) {
	    ModelAndView modelAndView = new ModelAndView("Message.html");
	    modelAndView.addObject("otherId", id);
	    return modelAndView;
	}
}
