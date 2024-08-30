package com.hokhanh.web.favorite.favoriteBeat;

import com.hokhanh.web.beat.PostedBeatResponse;
import com.hokhanh.web.common.Artist_Customer_Response;

public record FavoriteBeatResponse(
	Long id,
	Artist_Customer_Response accountOwner,
	PostedBeatResponse postedBeat
) {

}
