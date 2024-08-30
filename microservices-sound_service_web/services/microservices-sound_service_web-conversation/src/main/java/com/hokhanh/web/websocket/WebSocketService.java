package com.hokhanh.web.websocket;



import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hokhanh.web.conversation.ConversationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {
//	
//	private final SimpMessagingTemplate messagingTemplate;
//	
//	public void sendToMessageToSpecificUser(@Payload  ConversationResponse conversationResponse, String idAndRole) {
//		messagingTemplate.convertAndSendToUser(idAndRole, "/specific", conversationResponse);
//	}
	
}
