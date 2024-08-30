package com.hokhanh.web.notification.successfulPaymentNotification;


import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hokhanh.web.kafka.payment.PaymentCategory;
import com.hokhanh.web.kafka.payment.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "successful_payment_notifications")
public class SuccessfulPaymentNotification {

	@Id
	private String id;
	
	private Long paymentId;
	private double amount;
	private PaymentMethod paymentMethod;
	private PaymentCategory paymentCategory;
	private String sellerId;
	private String purchaserId;
	private LocalDate date;
}
