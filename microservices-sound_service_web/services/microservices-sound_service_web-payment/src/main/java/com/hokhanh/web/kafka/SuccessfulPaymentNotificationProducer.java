package com.hokhanh.web.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessfulPaymentNotificationProducer {

	private final KafkaTemplate<String, SuccessfulPaymentNotification> kafkaTemplate;
	
	public void sendSuccessfullPaymentNotification(SuccessfulPaymentNotification notification) {
		log.info("Sending Successfull Payment Notification with this paymentId: "+ notification.paymentId()); 
		
		Message<SuccessfulPaymentNotification> message = MessageBuilder
				.withPayload(notification)
				.setHeader(KafkaHeaders.TOPIC, "successful-payment-notification-topic")
				.build();
		kafkaTemplate.send(message);
	}
}
