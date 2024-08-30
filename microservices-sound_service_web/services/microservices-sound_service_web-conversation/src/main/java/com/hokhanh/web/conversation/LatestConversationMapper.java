package com.hokhanh.web.conversation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.ArtistCustomerResponse;


@Service
public class LatestConversationMapper {

	public LatestConversation toLatestConversation( String accountOwnerIdRole, String conversationId, LocalDateTime dateTime) {
		return LatestConversation.builder()
				.accountOwnerIdRole(accountOwnerIdRole)
				.conversationId(conversationId)
				.dateTime(dateTime)
				.build();
	}

	public LatestConversationResponse toLatestConversationResponse(LatestConversation latestConversation,
			ArtistCustomerResponse accountOwner, ConversationResponse conversationResponse) {
		return new LatestConversationResponse(latestConversation.getId(), accountOwner, conversationResponse, latestConversation.getDateTime());
	}

}
