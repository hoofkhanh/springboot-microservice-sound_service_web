package com.hokhanh.web.conversation;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ConversationRepository extends MongoRepository<Conversation, String> {

	@Query("{ $or: [ { senderIdRole: ?0, receiverIdRole: ?1 }, { senderIdRole: ?2, receiverIdRole: ?3 } ] }")
	Page<Conversation> findConversations(
	    String senderIdRole1,
	    String receiverIdRole1,
	    String senderIdRole2,
	    String receiverIdRole2,
	    Pageable pageable);
	
	List<Conversation> findBySenderIdRoleAndReceiverIdRoleAndIsReadFalse(
	    String senderIdRole,
		String receiverIdRole);

	 @Query("{ $or: [ { senderIdRole: ?0, receiverIdRole: ?1 }, { senderIdRole: ?2, receiverIdRole: ?3 } ] }")
	long countConversations(
			String senderIdRole1,
		    String receiverIdRole1,
		    String senderIdRole2,
		    String receiverIdRole2);
}
