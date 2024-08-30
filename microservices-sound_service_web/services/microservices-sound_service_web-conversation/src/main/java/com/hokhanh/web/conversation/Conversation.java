package com.hokhanh.web.conversation;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
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
@Document(collection = "conversations")
public class Conversation {

	@Id
	private String id;
	
	private String senderIdRole;
	private String receiverIdRole;
	private String message;
	private boolean isRead;
	
	@CreatedDate
    private LocalDateTime createdDate;
}
