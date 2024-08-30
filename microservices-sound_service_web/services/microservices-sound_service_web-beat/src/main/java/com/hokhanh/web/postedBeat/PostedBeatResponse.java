package com.hokhanh.web.postedBeat;

import java.time.LocalDate;

import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.SellerResponse;

public record PostedBeatResponse(
	Long id,
	BeatResponse beat,
	SellerResponse seller,
	double price,
	LocalDate postedDate
) {

}
