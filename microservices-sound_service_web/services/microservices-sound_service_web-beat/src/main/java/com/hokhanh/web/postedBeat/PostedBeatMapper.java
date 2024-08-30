package com.hokhanh.web.postedBeat;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hokhanh.web.beat.Beat;
import com.hokhanh.web.beat.BeatResponse;
import com.hokhanh.web.common.SellerResponse;

@Service
public class PostedBeatMapper {

	public PostedBeat toPostedBeat(PostedBeatRequest request, BeatResponse beatResponse) {
		return PostedBeat.builder()
				.beat(
						Beat.builder()
						.id(beatResponse.id())
						.build()
					)
				.sellerId(request.sellerId())
				.price(request.price())
				.postedDate(LocalDate.now())
				.build();
	}

	public PostedBeatResponse toPostedBeatResponse(PostedBeat postedBeat, SellerResponse sellerResponse, BeatResponse beatResponse) {
		return new PostedBeatResponse(
				postedBeat.getId(), 
				beatResponse, 
				sellerResponse, 
				postedBeat.getPrice(),
				postedBeat.getPostedDate()
				);
	}

}
