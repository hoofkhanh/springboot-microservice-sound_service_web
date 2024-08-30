package com.hokhanh.web.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {

	public User toUser(UserRequest request) {
		return User.builder()
				.id(request.id())
				.email(request.email())
				.password(request.password())
				.fullName(request.fullName())
				.phoneNumber(request.phoneNumber())
				.country(request.country())
				.dayOfBirth(request.dayOfBirth())
				.role(request.role())
				.build();
	}

	public UserResponse toUserResponse(User user) {
		return new UserResponse(
				user.getId(), 
				user.getEmail(), 
				user.getPassword(), 
				user.getFullName(), 
				user.getPhoneNumber(), 
				user.getCountry(), 
				user.getDayOfBirth(), 
				user.getRole());
	}

}
