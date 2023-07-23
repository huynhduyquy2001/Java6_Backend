package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
	SessionService sessionService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@GetMapping("/mess")
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("Message");
		return modelAndView;
	}

	@MessageMapping("/sendnewmess")
    @SendTo("/chat")
    public void addMess(@Payload MessageRequest messageRequest) {
		String senderId = messageRequest.getSenderId();
        String receiverId = messageRequest.getReceiverId();
        String content = messageRequest.getContent();
        // Thêm tin nhắn vào cơ sở dữ liệu
        Message newMessage = messageService.addMess(usersService.findUserById(senderId), usersService.findUserById(receiverId), content);
        // Gửi thông báo tới tất cả các client đang kết nối đến /chat
        messagingTemplate.convertAndSend("/chat", newMessage);
        
    }

	@GetMapping("/getmess")
	public List<Message> getListMess() {
		return messageService.getListMess("UI011", "UI010");
	}

	@GetMapping("/getmess2/{userId}")
	public List<Message> getListMess2(@PathVariable("userId") String userId) {
		return messageService.getListMess(sessionService.get("id"), userId);
	}

	@GetMapping("/getusersmess")
	public List<Object> getUsersMess() {
		return messageService.getListUsersMess(sessionService.get("id"));
	}

	@PostMapping("/getUser/{userId}")
	public Users findUserById(@PathVariable("userId") String userId) {
		return usersService.findUserById(userId);
	}
}
