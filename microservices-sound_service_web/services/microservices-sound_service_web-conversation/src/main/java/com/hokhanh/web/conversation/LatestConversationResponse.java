package com.hokhanh.web.conversation;

import java.time.LocalDateTime;

import com.hokhanh.web.common.ArtistCustomerResponse;

public record LatestConversationResponse(
	String id,
	ArtistCustomerResponse accountOwner,
	ConversationResponse conversation,
	LocalDateTime dateTime
) {

}
