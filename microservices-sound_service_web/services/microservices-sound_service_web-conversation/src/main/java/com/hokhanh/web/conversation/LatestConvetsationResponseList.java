package com.hokhanh.web.conversation;

import java.util.List;

public record LatestConvetsationResponseList(
	List<LatestConversationResponse> latestConversations,
	long total
) {

}
