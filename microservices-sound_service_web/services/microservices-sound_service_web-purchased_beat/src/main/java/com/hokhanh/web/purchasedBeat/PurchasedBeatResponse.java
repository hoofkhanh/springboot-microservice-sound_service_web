package com.hokhanh.web.purchasedBeat;

import java.time.LocalDate;

import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.Seller_Purchaser_Response;

public record PurchasedBeatResponse(
	Long id,
	BeatResponse beat,
	Seller_Purchaser_Response seller,
	Seller_Purchaser_Response purchaser,
	LocalDate purchaseDate,
	double purchasedPrice		
) {
	
}
