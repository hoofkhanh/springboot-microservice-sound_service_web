package com.hokhanh.web.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hokhanh.web.config.OAuthFeignConfig;

@FeignClient(
		name = "user-service",
		url = "${application.config.user-url}",
		configuration = OAuthFeignConfig.class
)
public interface UserClient {

	@PostMapping
	UserResponse createUser(@RequestBody UserRequest request);
	
	@PutMapping
	UserResponse updateUser(@RequestBody UserRequest request);
	
	@GetMapping("{user-id}")
	UserResponse findUserById(@PathVariable("user-id") String userId);
	
	@DeleteMapping("{user-id}")
	void deleteUserByID(@PathVariable("user-id") String userId);
}
