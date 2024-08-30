package com.hokhanh.web.notification;


import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hokhanh.web.kafka.purchasedBeat.user.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService service;
	
	@GetMapping("/find-by-artist-or-customer-id/{id}/{role}/{page}")
	public ResponseEntity<ListNotificationResponse> findNotificationByArtistIdOrCusgtomerId(@PathVariable("id") String id
			,@PathVariable("role") Role role,@PathVariable("page") int page) {
		return ResponseEntity.ok(new ListNotificationResponse(service.findNotificationByArtistIdOrCusgtomerId(id, role, page),
				service.countNotificationByArtistIdOrCusgtomerId(id, role), service.countNotificationByArtistIdOrCustomerIdUnRead(id, role)));
	}
	
	@PostMapping("/read-notifications")
	public ResponseEntity<Void> readNotification(@RequestBody Map<String, List<String>> request) {
		System.out.println(request.get("ids"));
		service.readNotification((List<String>) request.get("ids"));
		return ResponseEntity.accepted().build();
	}
}
