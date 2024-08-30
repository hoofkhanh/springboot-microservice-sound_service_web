package com.hokhanh.web.customer;

import com.hokhanh.web.user.UserRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
	
	String id,
	String image,
		
	@NotNull(message = "Artist User is required")
	@Valid
	UserRequest user,
	String token
) {

}
