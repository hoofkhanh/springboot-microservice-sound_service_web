package com.hokhanh.web.kafka.purchasedBeat.user;

import com.hokhanh.web.kafka.hire.common.ArtistType;

public record Seller_Purchaser_Response(
	String id,
	UserResponse user,
	ArtistType artistType,
	String artistName
) {

}
