package com.hokhanh.web.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HireProducer {

	private final KafkaTemplate<String, HireConfirmation> kafkaTemplate;
	
	public void sendHireConfirmationToMailOfSoundMakerChecker(HireConfirmation confirmation ) {
		log.info("Sending hireConfirmation");
		Message<HireConfirmation> message = MessageBuilder
				.withPayload(confirmation)
				.setHeader(KafkaHeaders.TOPIC, "hire-topic-mail-of-sound-maker-checker")
				.build();
		
		kafkaTemplate.send(message);
	}
	
	public void sendHireConfirmationToMailOfHirer(HireConfirmation confirmation ) {
		log.info("Sending hireConfirmation");
		Message<HireConfirmation> message = MessageBuilder
				.withPayload(confirmation)
				.setHeader(KafkaHeaders.TOPIC, "hire-topic-mail-of-hirer")
				.build();
		
		kafkaTemplate.send(message);
	}
}
