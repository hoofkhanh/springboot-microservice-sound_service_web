package com.hokhanh.web.purchasedBeat;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.Seller_Purchaser_Response;

@Service
public class PurchasedBeatMapper {

	public PurchasedBeat toPurchasedBeat(PurchasedBeatRequest request, double price) {
		return PurchasedBeat.builder()
				.beatId(request.beatId())
				.sellerId(request.sellerId())
				.purchaserId(request.purchaserId())
				.purchaseDate(LocalDate.now())
				.purchasedPrice(price)
				.build();
	}

	public PurchasedBeatResponse toPurchasedBeatResponse(PurchasedBeat purchasedBeat,
			BeatResponse beatResponse, 
			Seller_Purchaser_Response sellerResponse, Seller_Purchaser_Response purchaserResponse) {
		
		return new PurchasedBeatResponse(
					purchasedBeat.getId(),
					beatResponse,
					sellerResponse,
					purchaserResponse,
					purchasedBeat.getPurchaseDate(),
					purchasedBeat.getPurchasedPrice()
				);
	}

}
