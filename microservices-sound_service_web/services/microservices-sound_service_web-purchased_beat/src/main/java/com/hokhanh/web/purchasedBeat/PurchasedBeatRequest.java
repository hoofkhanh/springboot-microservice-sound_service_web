package com.hokhanh.web.purchasedBeat;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchasedBeatRequest(
		
	@NotNull(message = "PurchasedBeat beatId is required")
	Long beatId,
	
	@NotBlank(message = "PurchasedBeat sellerId is required")
	String sellerId,
	
	@NotBlank(message = "PurchasedBeat purchaserId is required")
	String purchaserId
	
) {

}
