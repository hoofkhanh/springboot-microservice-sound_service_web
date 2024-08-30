package com.hokhanh.web.conversation;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
public class ConversationController {

	private final ConversationService service;

	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/private")
	public ResponseEntity<ConversationResponse> addMessage(@Payload @Valid ConversationRequest request) {
		var conversation = service.addMessage(request);
		messagingTemplate.convertAndSendToUser(request.receiverIdRole(), "/specific", conversation);
		messagingTemplate.convertAndSendToUser(request.senderIdRole(), "/specific", conversation);
		return ResponseEntity.ok(conversation);
	}

	@GetMapping("/find-latest-conversation-by-account-owner-id-role/{account-owner-id-role}")
	public ResponseEntity<LatestConvetsationResponseList> findLatestConversationByAccountOwnerIdAndRole(
			@PathVariable("account-owner-id-role") String accountOwnerIdRole, @RequestParam("page") int page) {
		return ResponseEntity.ok(service.findLatestConversationByAccountOwnerIdAndRole(accountOwnerIdRole, page));
	}

	@GetMapping("/find-conversation-of-two-people/{person-1-id-role}/{person-2-id-role}")
	public ResponseEntity<ConversationResponseList> findAllConversationOfTwoPeople(
			@PathVariable("person-1-id-role") String person1IdRole,
			@PathVariable("person-2-id-role") String person2IdRole, @RequestParam("page") int page) {
		return ResponseEntity.ok(service.findConversationOfTwoPeople(person1IdRole, person2IdRole, page));
	}

	@GetMapping("/find-latest-conversation")
	public ResponseEntity<LatestConversationResponse> findLatestConversationByAccountOwnerIdRoleAndConversationId(
			@RequestParam("accountOwnerIdRole") String accountOwnerIdRole,
			@RequestParam("conversationId") String conversationId) {
		return ResponseEntity.ok(service.findLatestConversationByAccountOwnerIdRoleAndConversationId(accountOwnerIdRole,
				conversationId));
	}

	@GetMapping("/check-number-of-message-unread")
	public ResponseEntity<Long> checkNumberOfMessageUnread(
			@RequestParam("accountOwnerIdRole") String accountOwnerIdRole) {
		return ResponseEntity.ok(service.checkNumberOfMessageUnread(accountOwnerIdRole));
	}

	@PostMapping("/read-message")
	public ResponseEntity<Void> readMessage(@RequestBody Map<String, String> request) {
		service.readMessage(request.get("senderIdRole"), request.get("receiverIdRole"));
		return ResponseEntity.accepted().build();
	}

	@GetMapping("/find-latest-conversation-by-name")
	public ResponseEntity<List<LatestConversationResponse>> findLatestConversationByName(
			@RequestParam("accountOwnerIdRole") String accountOwnerIdRole, @RequestParam("name") String name) {
		return ResponseEntity.ok(service.findLatestConversationByName(accountOwnerIdRole, name));
	}

	
}
