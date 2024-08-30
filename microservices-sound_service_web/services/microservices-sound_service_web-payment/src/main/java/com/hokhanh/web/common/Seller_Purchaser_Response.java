package com.hokhanh.web.common;

import com.hokhanh.web.kafka.UserResponse;

public record Seller_Purchaser_Response(
	String id,
	UserResponse user,
	String artistName
) {

}
