package com.hokhanh.web.purchasedBeat;

import com.hokhanh.web.common.Seller_Purchaser_Response;

public record PurchasedBeatResponse(
	Long id,
	Seller_Purchaser_Response seller,
	Seller_Purchaser_Response purchaser,
	double purchasedPrice		
) {
	
}
