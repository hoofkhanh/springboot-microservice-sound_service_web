package com.hokhanh.web.purchasedBeat;



public record PurchasedBeatRequest(
		
	Long beatId,
	
	String sellerId,
	
	String purchaserId,
	
	double purchasedPrice
) {

}
