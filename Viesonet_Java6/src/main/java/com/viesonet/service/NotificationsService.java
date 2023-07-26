package com.viesonet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.NotificationsDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.NotificationType;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;

@Service
public class NotificationsService {
	@Autowired
	NotificationsDao notificationsDao;
	
	@Autowired
	UsersDao usersDao;
	
	public Notifications createNotifications(Users username, int count, String receiverId, Posts post, int notificationType) {
		Notifications notifications = new Notifications();
		Users user = new Users();
		if(notificationType == 1) {
			notifications.setNotificationContent(username.getUsername() + " vừa đăng một bài viết mới");
		}else if(notificationType == 2) {
			notifications.setNotificationContent(username.getUsername() +  " đã bắt đầu follow bạn");
		}else if(notificationType == 3) {
			if(count == 0) {
				notifications.setNotificationContent(username.getUsername() + " đã thích bài viết của bạn");
			}else {
					notifications.setNotificationContent(username.getUsername() + " và " + count + " người khác đã thích bài viết của bạn");
			}
		}else if(notificationType == 4) {
			if(count == 0) {
				notifications.setNotificationContent(username.getUsername() + " đã bình luận bài viết của bạn");
			}else {
				notifications.setNotificationContent(username.getUsername() + " và " + count + " người khác đã bình luận bài viết của bạn");
			}
		}else if(notificationType == 5) {
			notifications.setNotificationContent("Bài viết của bạn đã bị phạm!");
		}
		user.setUserId(receiverId);
		notifications.setReceiver(user);
		notifications.setPost(post);
		NotificationType nT = new NotificationType();
		nT.setTypeId(notificationType);
		notifications.setNotificationType(nT);
		Date date = new Date();
		notifications.setNotificationDate(date);
		notifications.setNotificationStatus(true);
		
		return notificationsDao.saveAndFlush(notifications);
	}
	
	public List<Notifications> findNotificationByReceiver(){
		return notificationsDao.findNotificationTrue();
	}
}
