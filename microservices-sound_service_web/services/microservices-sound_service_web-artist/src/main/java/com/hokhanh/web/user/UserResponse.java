package com.hokhanh.web.user;

import java.time.LocalDate;


public record UserResponse(
	String id,
	String email,
	String password,
	String fullName,
	String phoneNumber,
	String country,
	LocalDate dayOfBirth,
	Role role
) {




}
