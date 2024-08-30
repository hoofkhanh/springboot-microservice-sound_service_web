package com.hokhanh.web.common;

import com.hokhanh.web.user.UserResponse;

public record PosterResponse(
	String id,
	UserResponse user,
	String image
) {

}
