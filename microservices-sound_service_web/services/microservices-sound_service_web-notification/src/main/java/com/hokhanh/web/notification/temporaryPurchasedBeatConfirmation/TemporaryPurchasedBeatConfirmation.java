package com.hokhanh.web.notification.temporaryPurchasedBeatConfirmation;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "temporary_purchased_beat_confirmations")
public class TemporaryPurchasedBeatConfirmation {
	
	@Id
	private String id;
	private Long beatId;
	private String sellerId;
	private String purchaserId;
	private LocalDate purchaseDate;
	private double purchasedPrice;		
}
