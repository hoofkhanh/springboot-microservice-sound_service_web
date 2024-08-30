package com.hokhanh.web.user;


import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserSerivce service;
	
	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request){
		return ResponseEntity.ok(service.createUser(request));
	}
	
	@PutMapping
	public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest request){
		return ResponseEntity.ok(service.updateUser(request));
	}
	
	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll(){
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{user-id}")
	public ResponseEntity<UserResponse> findById(@PathVariable("user-id") String userId){
		return ResponseEntity.ok(service.findById(userId));
	}
	
//	@DeleteMapping("/{user-id}")
//	public ResponseEntity<Void> deleteByID(@PathVariable("user-id") String userId){
//		service.deleteByID(userId);
//		return ResponseEntity.accepted().build();
//	}
	

	
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@RequestBody Map<String, String> requestBody) {
		return ResponseEntity.ok(service.login(requestBody.get("email"), requestBody.get("password")));
	}
	
	@PostMapping("/generate-new-token")
	public ResponseEntity<Object> genreateNewToken(@RequestBody Map<String, String> requestBody) {
		return ResponseEntity.ok(service.genreateNewToken(requestBody.get("email"), requestBody.get("password"), 
				requestBody.get("client_id"), requestBody.get("client_secret"), requestBody.get("grant_type")));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody Map<String, String> requestBody) {
		service.logout(requestBody.get("client_id"), requestBody.get("client_secret"), requestBody.get("refresh_token"));
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/introspect-token")
	public ResponseEntity<String> introspectToken(@RequestBody Map<String, String> requestBody) {
		return ResponseEntity.ok(service.introspectToken( requestBody.get("client_id"), 
				requestBody.get("client_secret"), requestBody.get("token")));
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<Object> refreshToken(@RequestBody Map<String, String> requestBody) {
		return ResponseEntity.ok(service.refreshToken(requestBody.get("client_id"), requestBody.get("client_secret"), 
				requestBody.get("refresh_token"), requestBody.get("grant_type")));
	}
	
	@PostMapping("/get-token-no-need-login")
	public ResponseEntity<Object> getTokenNoNeedLogin(@RequestBody Map<String, String> requestBody) {
		return ResponseEntity.ok(service.getTokenNoNeedLogin( 
				requestBody.get("client_id"), requestBody.get("client_secret"), requestBody.get("grant_type")));
	}
	
	@GetMapping("/get-last-access-of-user/keycloak")
	public ResponseEntity<Object> getLastAccessOfUser(@RequestParam Map<String, String> requestBody) throws URISyntaxException, IOException, InterruptedException {
		System.out.println(requestBody.get("token"));
		return ResponseEntity.ok(service.getLastAccessOfUser( 
				requestBody.get("client_id_real"), requestBody.get("token")));
	}
	
}
