package com.hokhanh.web.conversation;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.ArtistCustomerResponse;


@Service
public class ConversationMapper {

	public Conversation toConversation(ConversationRequest request) {
		return Conversation.builder()
				.senderIdRole(request.senderIdRole())
				.receiverIdRole(request.receiverIdRole())
				.message(request.message())
				.isRead(false)
				.build();
	}

	public ConversationResponse toConversationResponse(Conversation conversation, ArtistCustomerResponse sender,
			ArtistCustomerResponse receiver) {
		return new ConversationResponse(conversation.getId(), sender, receiver, 
				conversation.getMessage(), conversation.isRead(), conversation.getCreatedDate());
	}

}
