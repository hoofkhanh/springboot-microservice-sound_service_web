package com.hokhanh.web.conversation;

import java.time.LocalDateTime;

import com.hokhanh.web.common.ArtistCustomerResponse;

public record ConversationResponse(
	String id,
	ArtistCustomerResponse sender,
	ArtistCustomerResponse receiver,
	String message,
	boolean isRead,
	LocalDateTime createdDate
) {

}
