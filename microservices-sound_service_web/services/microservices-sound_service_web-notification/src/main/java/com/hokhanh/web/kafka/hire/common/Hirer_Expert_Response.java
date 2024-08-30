package com.hokhanh.web.kafka.hire.common;

import com.hokhanh.web.kafka.purchasedBeat.user.UserResponse;

public record Hirer_Expert_Response(
	String id,
	double hireCost,
	UserResponse user,
	ArtistType artistType
) {

}
