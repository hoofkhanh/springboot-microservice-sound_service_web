package com.hokhanh.web.notification;

import java.time.LocalDate;
import java.time.LocalTime;

import com.hokhanh.web.kafka.hire.HireConfirmation;
import com.hokhanh.web.kafka.payment.SuccessfulPaymentNotification;
import com.hokhanh.web.kafka.purchasedBeat.TemporaryPurchasedBeatConfirmation;

public record NotificationResponse(
	String id,
	TemporaryPurchasedBeatConfirmation temporaryPurchasedBeatConfirmation,
	HireConfirmation hireConfirmation,
	SuccessfulPaymentNotification successfullPaymentNotification,
	NotificationType type,
	LocalDate notificationDate,
	LocalTime notificationTime,
	String receiverIdRole,
	boolean isRead
	
) {

}
