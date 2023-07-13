package com.viesonet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.MessageDao;
import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;

@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;
	public List<Message> getListMess(String senderId, String receiverId) {
		return messageDao.getListMess(senderId, receiverId);
	}
	
	// Trong phương thức trong service hoặc controller
	public List<Object> getListUsersMess(String userId) {
	    List<Object> result = messageDao.getListUsersMess(userId);
	    Set<String> uniquePairs = new HashSet<>();
	    List<Object> uniqueRows = new ArrayList<>();

	    for (Object row : result) {
	        Object[] rowData = (Object[]) row;
	        String pair1 = rowData[0] + "-" + rowData[2];
	        String pair2 = rowData[2] + "-" + rowData[0];
	        if (!uniquePairs.contains(pair1) && !uniquePairs.contains(pair2)) {
	            uniqueRows.add(row);
	            uniquePairs.add(pair1);
	        }
	    }

	    return uniqueRows;
	}

}
