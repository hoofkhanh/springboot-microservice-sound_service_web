package com.hokhanh.web.beat;

import java.time.LocalDate;


public record PostedBeatResponse(
	Long id,
	BeatResponse beat,
	SellerResponse seller,
	double price,
	LocalDate postedDate
) {

}
