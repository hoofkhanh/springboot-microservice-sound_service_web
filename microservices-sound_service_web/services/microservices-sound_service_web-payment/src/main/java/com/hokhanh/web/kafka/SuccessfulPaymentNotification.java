package com.hokhanh.web.kafka;

import java.time.LocalDate;

import com.hokhanh.web.common.Seller_Purchaser_Response;
import com.hokhanh.web.payment.PaymentMethod;

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
