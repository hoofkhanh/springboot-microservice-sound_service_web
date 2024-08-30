package com.hokhanh.web.conversation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LatestConversationRepository extends MongoRepository<LatestConversation, String> {

	List<LatestConversation> findByConversationId(String conversationId);

	Page<LatestConversation> findByAccountOwnerIdRole(String accountOwnerIdRole, Pageable pageable);
	
	List<LatestConversation> findByAccountOwnerIdRole(String accountOwnerIdRole);

	LatestConversation findByAccountOwnerIdRoleAndConversationId(String accountOwnerIdRole, String conversationId);

	
}
