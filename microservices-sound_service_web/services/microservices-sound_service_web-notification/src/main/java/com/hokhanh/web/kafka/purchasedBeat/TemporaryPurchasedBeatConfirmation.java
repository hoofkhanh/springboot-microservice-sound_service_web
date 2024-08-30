package com.hokhanh.web.kafka.purchasedBeat;

import java.time.LocalDate;

import com.hokhanh.web.kafka.purchasedBeat.beat.BeatResponse;
import com.hokhanh.web.kafka.purchasedBeat.user.Seller_Purchaser_Response;



public record TemporaryPurchasedBeatConfirmation(
	long temporaryPurchasedBeatid,
	BeatResponse beat,
	Seller_Purchaser_Response seller,
	Seller_Purchaser_Response purchaser,
	LocalDate purchaseDate,
	double purchasedPrice		
) {

}
