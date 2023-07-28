package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Notifications;

public interface NotificationsDao extends JpaRepository<Notifications, Integer> {
	@Query("SELECT n FROM Notifications n WHERE n.notificationStatus = true")
	List<Notifications> findNotificationTrue();
	
	@Query("SELECT n FROM Notifications n WHERE n.receiver.userId =:userId")
	List<Notifications> findAllByReceiver(String userId);
	
	Notifications findByNotificationId(int notificationId);
}
