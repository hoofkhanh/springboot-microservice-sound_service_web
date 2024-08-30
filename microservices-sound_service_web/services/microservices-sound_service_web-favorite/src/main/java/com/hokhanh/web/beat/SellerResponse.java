package com.hokhanh.web.beat;

import com.hokhanh.web.user.UserResponse;

public record SellerResponse(
	String id,
	UserResponse user
) {

}
