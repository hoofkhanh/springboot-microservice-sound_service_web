package com.hokhanh.web.kafka;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.beat.BeatClient;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.email.EmailService;
import com.hokhanh.web.exception.NotificationException;
import com.hokhanh.web.jobType.JobTypeClient;
import com.hokhanh.web.kafka.hire.HireConfirmation;
import com.hokhanh.web.kafka.payment.SuccessfulPaymentNotification;
import com.hokhanh.web.kafka.purchasedBeat.TemporaryPurchasedBeatConfirmation;
import com.hokhanh.web.notification.Notification;
import com.hokhanh.web.notification.NotificationRepository;
import com.hokhanh.web.notification.NotificationResponse;
import com.hokhanh.web.notification.NotificationService;
import com.hokhanh.web.notification.NotificationType;
import com.hokhanh.web.notification.hireConfirmation.HireConfirmationRepository;
import com.hokhanh.web.notification.successfulPaymentNotification.SuccessfulPaymentNotificationRepository;
import com.hokhanh.web.notification.temporaryPurchasedBeatConfirmation.TemporaryPurchasedBeatConfirmationRepository;
import com.hokhanh.web.websocket.WebSocketService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
	
	private final NotificationRepository repo;
	
	private final TemporaryPurchasedBeatConfirmationRepository temporaryPurchasedBeatConfirmationRepo;
	
	private final SuccessfulPaymentNotificationRepository successfulPaymentNotificationRepository;
	
	private final HireConfirmationRepository hireConfirmationRepository;
	
	private final EmailService emailService;
	
	private final WebSocketService webSocketService;
	
	@KafkaListener(topics = "temporary-purchased-beat-topic")
	public void consumeTemporaryPurchasedBeat(TemporaryPurchasedBeatConfirmation confirmation) throws MessagingException {
		
		var tempPurchasedBeatConfirmation = temporaryPurchasedBeatConfirmationRepo.save( com.hokhanh.web.notification
				.temporaryPurchasedBeatConfirmation.TemporaryPurchasedBeatConfirmation.builder()
			.beatId(confirmation.beat().id())
			.sellerId(confirmation.seller().id())
			.purchaserId(confirmation.purchaser().id())
			.purchaseDate(confirmation.purchaseDate())
			.purchasedPrice(confirmation.purchasedPrice())	
			.build()
		);
		
		var notification = repo.save(
				Notification.builder()
				.temporaryPurchasedBeatConfirmationId(tempPurchasedBeatConfirmation.getId())
				.type(NotificationType.TEMPORARY_PURCHASED_BEAT_CONFIRMATION)
				.notificationDate(LocalDate.now())
				.notificationTime(LocalTime.now())
				.receiverIdRole(confirmation.purchaser().id() + " " + confirmation.purchaser().user().role())
				.isRead(false)
				.build()
			);
		
		emailService.sendPurchasedBeatConfirmation(confirmation);

		var realBeatConfirmation = new TemporaryPurchasedBeatConfirmation( 0,
				confirmation.beat(), confirmation.seller(), confirmation.purchaser(),
				confirmation.purchaseDate(),
				confirmation.purchasedPrice());
		
		var notificationResponse = new NotificationResponse(notification.getId(), realBeatConfirmation, 
				null, null,
				notification.getType(), notification.getNotificationDate(), notification.getNotificationTime(),
				notification.getReceiverIdRole(), notification.isRead());
		
		webSocketService.sendToNotificationToSpecificUser(notificationResponse, 
				confirmation.purchaser().id() + " " + confirmation.purchaser().user().role());
		
	}
	
	@KafkaListener(topics = "successful-payment-notification-topic")
	public void consumeSuccessfulPaymentNotification(SuccessfulPaymentNotification notification) throws MessagingException {
		
		var successfulPaymentNotification = successfulPaymentNotificationRepository.save( 
				com.hokhanh.web.notification.successfulPaymentNotification.SuccessfulPaymentNotification
				.builder()
			.paymentId(notification.paymentId())
			.amount(notification.amount())
			.paymentMethod(notification.paymentMethod())
			.paymentCategory(notification.category())
			.sellerId(notification.seller().id())
			.purchaserId(notification.purchaser().id())
			.date(notification.date())
			.build()
		);
		
		var notificationOfSeller = repo.save(
				Notification.builder()
				.successfullPaymentNotificationId(successfulPaymentNotification.getId())
				.type(NotificationType.SUCCESSFULL_PAYMENT_NOTIFICATION)
				.notificationDate(LocalDate.now())
				.notificationTime(LocalTime.now())
				.receiverIdRole(notification.seller().id() + " " + notification.seller().user().role())
				.isRead(false)
				.build()
			);
		
		var notificationOfPurchaser =repo.save(
				Notification.builder()
				.successfullPaymentNotificationId(successfulPaymentNotification.getId())
				.type(NotificationType.SUCCESSFULL_PAYMENT_NOTIFICATION)
				.notificationDate(LocalDate.now())
				.notificationTime(LocalTime.now())
				.receiverIdRole(notification.purchaser().id() + " " + notification.purchaser().user().role())
				.isRead(false)
				.build()
			);
		
		emailService.sendSuccessfulPaymentNotification(notification);
	
		var realSuccessfulPaymentNotification = new SuccessfulPaymentNotification(notification.paymentId(),
				notification.amount(), notification.paymentMethod(), 
				notification.category(), notification.seller(), notification.purchaser(), notification.date());
		
		var notificationResponse = new NotificationResponse(notificationOfSeller.getId(), null, 
				null, realSuccessfulPaymentNotification,
				notificationOfSeller.getType(), notificationOfSeller.getNotificationDate(), notificationOfSeller.getNotificationTime(),
				notificationOfSeller.getReceiverIdRole(), notificationOfSeller.isRead());
		
		webSocketService.sendToNotificationToSpecificUser(notificationResponse, 
				notification.seller().id() + " " + notification.seller().user().role());
		
		notificationResponse = new NotificationResponse(notificationOfPurchaser.getId(), null, 
				null, realSuccessfulPaymentNotification,
				notificationOfPurchaser.getType(), notificationOfPurchaser.getNotificationDate(), notificationOfPurchaser.getNotificationTime(),
				notificationOfPurchaser.getReceiverIdRole(), notificationOfPurchaser.isRead());
		
		webSocketService.sendToNotificationToSpecificUser(notificationResponse, 
				notification.purchaser().id() + " " + notification.purchaser().user().role());
	}
	
	@KafkaListener(topics = "hire-topic-mail-of-sound-maker-checker")
	public void consumeHireConfirmationToMailOfSoundMakerChecker(HireConfirmation confirmation) throws MessagingException {
		
		var hireConfirmation = hireConfirmationRepository.save(
				com.hokhanh.web.notification.hireConfirmation.HireConfirmation
				.builder()
				.expertId(confirmation.expert().id())
				.hirerId(confirmation.hirer().id())
				.jobTypeId(confirmation.jobType() != null ? confirmation.jobType().id() : null)
				.price(confirmation.price())
				.startDate(confirmation.startDate())
				.endDate(confirmation.endDate())
				.build()
			);
		
		var notification = repo.save(
				Notification.builder()
				.hireConfirmationID(hireConfirmation.getId())
				.type(NotificationType.HIRE_CONFIRMATION)
				.notificationDate(LocalDate.now())
				.notificationTime(LocalTime.now())
				.receiverIdRole(confirmation.expert().id() + " " + confirmation.expert().user().role())
				.isRead(false)
				.build()
			);
		
		emailService.sendHireConfirmationToMailSoundMakerChecker(confirmation);
		
		var realHireConfirmation = new HireConfirmation(confirmation.hirer(), confirmation.expert(), 
				confirmation.jobType(), confirmation.startDate()
				, confirmation.endDate()
				, confirmation.price());
		
		var notificationResponse = new NotificationResponse(notification.getId(), null, 
				realHireConfirmation, null,
				notification.getType(), notification.getNotificationDate(), notification.getNotificationTime(),
				notification.getReceiverIdRole(), notification.isRead());
		
		webSocketService.sendToNotificationToSpecificUser(notificationResponse, 
				confirmation.expert().id() + " " + confirmation.expert().user().role());
	}
	
	@KafkaListener(topics = "hire-topic-mail-of-hirer")
	public void consumeHireConfirmationToMailOfHirer(HireConfirmation confirmation) throws MessagingException {
		var hireConfirmation = hireConfirmationRepository.save(
				com.hokhanh.web.notification.hireConfirmation.HireConfirmation
				.builder()
				.expertId(confirmation.expert().id())
				.hirerId(confirmation.hirer().id())
				.jobTypeId(confirmation.jobType() != null ? confirmation.jobType().id() : null)
				.price(confirmation.price())
				.startDate(confirmation.startDate())
				.endDate(confirmation.endDate())
				.build()
			);
		
		
		var notification = repo.save(
				Notification.builder()
				.hireConfirmationID(hireConfirmation.getId())
				.type(NotificationType.HIRE_CONFIRMATION)
				.notificationDate(LocalDate.now())
				.notificationTime(LocalTime.now())
				.receiverIdRole(confirmation.hirer().id()+ " " + confirmation.hirer().user().role())
				.isRead(false)
				.build()
			);
		
		emailService.sendHireConfirmationToMailOfHirer(confirmation);
		
		var realHireConfirmation = new HireConfirmation(confirmation.hirer(), confirmation.expert(), 
				confirmation.jobType(), confirmation.startDate()
				, confirmation.endDate()
				, confirmation.price());
		
		var notificationResponse = new NotificationResponse(notification.getId(), null, 
				realHireConfirmation, null,
				notification.getType(), notification.getNotificationDate(), notification.getNotificationTime(),
				notification.getReceiverIdRole(), notification.isRead());
		
		webSocketService.sendToNotificationToSpecificUser(notificationResponse, 
				confirmation.hirer().id()+ " " + confirmation.hirer().user().role());
	}
}
