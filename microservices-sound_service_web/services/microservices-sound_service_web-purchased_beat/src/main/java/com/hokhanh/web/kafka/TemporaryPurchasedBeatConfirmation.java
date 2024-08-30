package com.hokhanh.web.kafka;

import java.time.LocalDate;

import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.Seller_Purchaser_Response;

public record TemporaryPurchasedBeatConfirmation(
	Long temporaryPurchasedBeatid,
	BeatResponse beat,
	Seller_Purchaser_Response seller,
	Seller_Purchaser_Response purchaser,
	LocalDate purchaseDate,
	double purchasedPrice		
) {

}
