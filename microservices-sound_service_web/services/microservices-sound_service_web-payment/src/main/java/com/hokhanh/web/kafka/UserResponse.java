package com.hokhanh.web.kafka;



public record UserResponse(
	String id,
	String email,
	String fullName,
	String country,
	Role role
) {

}
