package com.hokhanh.web.conversation;

import java.util.List;

public record ConversationResponseList(
	List<ConversationResponse> conversations,
	long total
) {

}
