package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Message;
import com.viesonet.entity.UserMessage;

public interface MessageDao extends JpaRepository<Message, Integer> {

	@Query("Select obj from Message obj where (obj.sender.userId=?1 and obj.receiver.userId=?2) or"
			+ "(obj.sender.userId=?2 and obj.receiver.userId=?1)")
	List<Message> getListMess(String senderId, String receiverId);

	@Query("SELECT DISTINCT m.sender.userId, m.sender.username, m.receiver.userId, m.receiver.username, m.sender.avatar, m.receiver.avatar, m.content,  m.sendDate, m.status, m.messId FROM Message m WHERE m.receiver.userId = ?1 OR m.sender.userId = ?1 order by m.sendDate desc ")
	List<Object> getListUsersMess(String userId);

	@Query("SELECT count(*) FROM Message list where list.receiver.userId = ?1 and list.status = ?2")
	int getListUnseenMessage(String userId, String chuoi);
	
}
