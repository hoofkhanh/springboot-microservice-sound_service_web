package com.hokhanh.web.common;


import com.hokhanh.web.artist.ArtistType;
import com.hokhanh.web.user.UserResponse;

public record Artist_Customer_Response(
	String id,
	String image,
	String artistName,
	UserResponse user,
	ArtistType artistType
) {

}
