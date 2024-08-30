package com.hokhanh.web.conversation;

import jakarta.validation.constraints.NotBlank;

public record ConversationRequest(
	String oldId,
	
	@NotBlank(message = "sender id and role is mandatory")
	String senderIdRole,
	
	@NotBlank(message = "receiver id and role is mandatory")
	String receiverIdRole,
	
	@NotBlank(message = "message is mandatory")
	String message
	

) {

}
