package com.hokhanh.web.common;


import com.hokhanh.web.user.UserResponse;

public record ArtistCustomerResponse(
	String id,
	UserResponse user,
	String image,
	String artistName
) {

}
