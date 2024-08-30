package com.hokhanh.web.hire.common;

import com.hokhanh.web.kafka.UserResponse;

public record Hirer_Expert_Response(
	String id,
	double hireCost,
	UserResponse user,
	String artistName
) {

}
