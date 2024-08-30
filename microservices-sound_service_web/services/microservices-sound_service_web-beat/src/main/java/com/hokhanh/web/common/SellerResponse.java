package com.hokhanh.web.common;

import com.hokhanh.web.user.UserResponse;

public record SellerResponse(
	String id,
	String artistName,
	UserResponse user
) {

}
