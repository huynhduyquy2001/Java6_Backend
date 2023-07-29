package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageTemplateService {
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	public void convertAndSend(String n, String m) {
		messagingTemplate.convertAndSend(n,m);
	}
}
