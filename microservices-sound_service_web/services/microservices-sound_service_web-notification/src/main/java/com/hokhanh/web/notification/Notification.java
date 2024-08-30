package com.hokhanh.web.notification;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Document(collection = "notifications")
public class Notification {

	@Id
	private String id;
	private String temporaryPurchasedBeatConfirmationId;
	private String hireConfirmationID;
	private String successfullPaymentNotificationId;
	private NotificationType type;
	private LocalDate notificationDate;
	private LocalTime notificationTime;
	private String receiverIdRole;
	private boolean isRead;
}
