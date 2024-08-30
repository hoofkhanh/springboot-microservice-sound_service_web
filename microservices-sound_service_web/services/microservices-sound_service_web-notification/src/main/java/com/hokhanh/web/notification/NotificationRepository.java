package com.hokhanh.web.notification;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	
	List<Notification> findByReceiverIdRole(String idRole, Pageable pageable);

	long countByReceiverIdRole(String idRole);
	
	long countByReceiverIdRoleAndIsReadFalse(String idRole);
}
