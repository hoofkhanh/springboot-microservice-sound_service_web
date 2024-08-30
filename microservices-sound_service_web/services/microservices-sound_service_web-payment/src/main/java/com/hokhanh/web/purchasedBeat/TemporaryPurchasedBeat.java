package com.hokhanh.web.purchasedBeat;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TemporaryPurchasedBeat {

	private Long id;
	
	private Long beatId;
	private String sellerId;
	private String purchaserId;
	
	private LocalDate purchaseDate;
	private double purchasedPrice;
}
