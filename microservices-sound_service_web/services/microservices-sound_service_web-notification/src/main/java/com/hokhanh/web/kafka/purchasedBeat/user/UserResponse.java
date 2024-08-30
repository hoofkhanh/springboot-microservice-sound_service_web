package com.hokhanh.web.kafka.purchasedBeat.user;



public record UserResponse(
	String id,
	String email,
	String fullName,
	String country,
	Role role
) {

}
