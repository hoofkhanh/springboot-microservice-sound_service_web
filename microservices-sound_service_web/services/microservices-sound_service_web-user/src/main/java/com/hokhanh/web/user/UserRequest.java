package com.hokhanh.web.user;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
		
	String id,
		
	@NotBlank(message = "User email is required")
	String email,
	
	@NotBlank(message = "User password is required")
	String password,
	
	@NotBlank(message = "User fullName is required")
	String fullName,
	
	@NotBlank(message = "User phoneNumber is required")
	String phoneNumber,
	
	@NotBlank(message = "User country is required")
	String country,
	
	@NotNull(message = "User dayOfBirth is required")
	LocalDate dayOfBirth,
	
	@NotNull(message = "User role is required")
	Role role
) {

	
}
