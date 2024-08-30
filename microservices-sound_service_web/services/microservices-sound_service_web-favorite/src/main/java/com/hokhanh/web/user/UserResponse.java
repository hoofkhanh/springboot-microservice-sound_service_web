package com.hokhanh.web.user;



public record UserResponse(
	String id,
	String email,
	String fullName,
	String country,
	Role role
) {

}
