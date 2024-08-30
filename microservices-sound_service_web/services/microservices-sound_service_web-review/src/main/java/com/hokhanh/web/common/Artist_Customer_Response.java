package com.hokhanh.web.common;


import com.hokhanh.web.user.UserResponse;

public record Artist_Customer_Response(
	String id,
	UserResponse user,
	String image
) {

}
