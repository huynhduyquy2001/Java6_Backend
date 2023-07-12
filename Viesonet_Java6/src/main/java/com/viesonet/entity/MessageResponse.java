package com.viesonet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MessageResponse {
    private String recipientId;
    private String content;
    private String status;
	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public MessageResponse(String recipientId, String content, String status) {
		super();
		this.recipientId = recipientId;
		this.content = content;
		this.status = status;
	}
	public MessageResponse( String content) {
		super();
		
		this.content = content;
		
	}
	public MessageResponse() {
		
		
	}

    // Constructors, getters, and setters
}

