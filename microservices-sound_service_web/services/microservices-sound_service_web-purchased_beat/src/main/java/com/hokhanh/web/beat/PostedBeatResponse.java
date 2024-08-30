package com.hokhanh.web.beat;

import java.time.LocalDate;

import com.hokhanh.web.common.Seller_Purchaser_Response;

public record PostedBeatResponse(
	Long id,
	BeatResponse beat,
	Seller_Purchaser_Response seller,
	double price,
	LocalDate postedDate
) {

}
