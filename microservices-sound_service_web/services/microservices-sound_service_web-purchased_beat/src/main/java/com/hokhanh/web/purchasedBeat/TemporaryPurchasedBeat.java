package com.hokhanh.web.purchasedBeat;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "temporary_purchased_beats")
@Builder
public class TemporaryPurchasedBeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long beatId;
	private String sellerId;
	private String purchaserId;
	
	private LocalDate purchaseDate;
	private double purchasedPrice;
}
