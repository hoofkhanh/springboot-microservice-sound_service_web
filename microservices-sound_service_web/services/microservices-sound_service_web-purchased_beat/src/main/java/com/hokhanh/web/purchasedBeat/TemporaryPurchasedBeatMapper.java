package com.hokhanh.web.purchasedBeat;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.Seller_Purchaser_Response;

@Service
public class TemporaryPurchasedBeatMapper {

	public TemporaryPurchasedBeat toTempPurchasedBeat(TemporaryPurchasedBeatRequest request, double price) {
		return TemporaryPurchasedBeat.builder()
				.beatId(request.beatId())
				.sellerId(request.sellerId())
				.purchaserId(request.purchaserId())
				.purchaseDate(LocalDate.now())
				.purchasedPrice(price)
				.build();
	}
	
	public TemporaryPurchasedBeatResponse toTempPurchasedBeatResponse(TemporaryPurchasedBeat tempPurchasedBeat,
			BeatResponse beatResponse, 
			Seller_Purchaser_Response sellerResponse, Seller_Purchaser_Response purchaserResponse) {
		
		return new TemporaryPurchasedBeatResponse(
					tempPurchasedBeat.getId(),
					beatResponse,
					sellerResponse,
					purchaserResponse,
					tempPurchasedBeat.getPurchaseDate(),
					tempPurchasedBeat.getPurchasedPrice()
				);
	}
}
