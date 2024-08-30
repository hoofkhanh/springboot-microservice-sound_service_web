package com.hokhanh.web.kafka.payment;

import java.time.LocalDate;

import com.hokhanh.web.kafka.purchasedBeat.user.Seller_Purchaser_Response;

public record SuccessfulPaymentNotification(
	Long paymentId,
	double amount,
	PaymentMethod paymentMethod,
	PaymentCategory category,
	Seller_Purchaser_Response seller,
	Seller_Purchaser_Response purchaser,
	LocalDate date
) {
	
}
