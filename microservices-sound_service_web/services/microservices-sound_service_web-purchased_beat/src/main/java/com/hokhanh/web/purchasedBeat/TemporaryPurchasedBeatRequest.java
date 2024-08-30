package com.hokhanh.web.purchasedBeat;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TemporaryPurchasedBeatRequest(
		
	@NotNull(message = "TemporaryPurchasedBeat beatId is required")
	Long beatId,
	
	@NotBlank(message = "TemporaryPurchasedBeat sellerId is required")
	String sellerId,
	
	@NotBlank(message = "TemporaryPurchasedBeat purchaserId is required")
	String purchaserId
	
) {

}
