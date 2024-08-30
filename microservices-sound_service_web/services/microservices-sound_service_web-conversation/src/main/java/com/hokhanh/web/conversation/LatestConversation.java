package com.hokhanh.web.conversation;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "latest_conversations")
public class LatestConversation {

	@Id
	private String id;
	
	private String accountOwnerIdRole;
	
	private String conversationId;
	
	private LocalDateTime dateTime;
}
