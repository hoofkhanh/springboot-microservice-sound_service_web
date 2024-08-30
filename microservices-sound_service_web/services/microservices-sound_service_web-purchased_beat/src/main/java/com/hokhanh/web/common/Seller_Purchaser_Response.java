package com.hokhanh.web.common;

import com.hokhanh.web.artist.ArtistType;
import com.hokhanh.web.user.UserResponse;

public record Seller_Purchaser_Response(
	String id,
	UserResponse user,
	String artistName,
	ArtistType artistType
) {

}
