package com.hokhanh.web.favorite.favoriteBeat;

import org.springframework.stereotype.Service;

import com.hokhanh.web.beat.PostedBeatResponse;
import com.hokhanh.web.common.Artist_Customer_Response;

@Service
public class FavoriteBeatMapper {

	public FavoriteBeat toFavoriteBeat(FavoriteBeatRequest request) {
		return FavoriteBeat.builder()
				.accountOwnerId(request.accountOwnerId())
				.postedBeatId(request.postedBeatId())
				.build();
	}

	public FavoriteBeatResponse toFavoriteBeatResponse(FavoriteBeat favoriteBeat,
			Artist_Customer_Response accountOwnerResponse, PostedBeatResponse postedBeatResponse) {
		return new FavoriteBeatResponse(
				favoriteBeat.getId(), 
				accountOwnerResponse, 
				postedBeatResponse
				);
	}

}
