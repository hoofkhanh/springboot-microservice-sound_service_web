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
public class TemporaryPurchasedBeatProducer {

	private final KafkaTemplate<String, TemporaryPurchasedBeatConfirmation> kafkaTemplate;
	
	public void sendTemporaryPurchasedBeatConfirmation(TemporaryPurchasedBeatConfirmation confirmation) {
		log.info("Sending purchased beat confirmation with this temporaryPurchasedBeatid: "+ confirmation.temporaryPurchasedBeatid()); 
		Message<TemporaryPurchasedBeatConfirmation> message = MessageBuilder
				.withPayload(confirmation)
				.setHeader(KafkaHeaders.TOPIC, "temporary-purchased-beat-topic")
				.build();
		kafkaTemplate.send(message);
	}
}
