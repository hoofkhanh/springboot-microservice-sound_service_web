package com.hokhanh.web.customer;

import com.hokhanh.web.user.UserResponse;

public record CustomerResponse(
	
	String id,
	String image,
	UserResponse user
) {

}
