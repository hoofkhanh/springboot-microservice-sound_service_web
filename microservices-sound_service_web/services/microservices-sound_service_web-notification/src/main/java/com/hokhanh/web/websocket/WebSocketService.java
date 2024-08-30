package com.hokhanh.web.websocket;


import java.util.List;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hokhanh.web.notification.NotificationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {

	private final SimpMessagingTemplate messagingTemplate;
	
	public void sendToNotificationToSpecificUser(@Payload NotificationResponse notificationResponse, String idAndRole) {
		messagingTemplate.convertAndSendToUser(idAndRole, "/topic/notifications", notificationResponse);
	}
	
}
