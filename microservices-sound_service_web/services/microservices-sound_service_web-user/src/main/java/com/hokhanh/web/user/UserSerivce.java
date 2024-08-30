package com.hokhanh.web.user;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.exception.UserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSerivce {
	
	private final UserRepository repo;
	private final UserMapper mapper;
	
	@Transactional
	public UserResponse createUser( UserRequest request) {
		if(repo.findByEmail(request.email()) != null) {
			throw new UserException("Can't create user because Email is exist");
		}
		
		if(repo.findByPhoneNumber(request.phoneNumber()) != null) {
			throw new UserException("Can't create user because Phone Number is exist");
		}
		
		var user = repo.save(mapper.toUser(request));
		return mapper.toUserResponse(user);
	}

	@Transactional
	public UserResponse updateUser( UserRequest request) {
		if(request.id() == null ) {
			throw new UserException(
					String.format("Need id to update user")
				);
		}
		
		var user = repo.findById(request.id()).orElseThrow(() -> new UserException(
					String.format("Can't update user because no User found with the provided ID: %s", request.id())
				));
		
		var checkByPhoneNumber = repo.findByPhoneNumber(request.phoneNumber());
		if(checkByPhoneNumber != null && checkByPhoneNumber.getId().equals(request.id())== false) {
			throw new UserException(
					String.format("Can't update user because Phone Number is exist")
				);
		}
		
		var checkByEmail = repo.findByEmail(request.email());
		if(checkByEmail != null && checkByEmail.getId().equals(request.id())== false) {
			throw new UserException(
					String.format("Can't update user because Email is exist")
				);
		}
		
		user.setEmail(request.email());
		user.setPassword(request.password());
		user.setFullName(request.fullName());
		user.setPhoneNumber(request.phoneNumber());
		user.setCountry(request.country());
		user.setDayOfBirth(request.dayOfBirth());
		user.setRole(request.role());
		return mapper.toUserResponse(repo.save(user));
	}

	public List<UserResponse> findAll() {
		return repo.findAll()
				.stream()
				.map(user -> mapper.toUserResponse(user))
				.toList();
	}

	public UserResponse findById(String userId) {
		return repo.findById(userId)
				.map(user -> mapper.toUserResponse(user))
				.orElseThrow(() -> new UserException(
							String.format("No user found with the provided ID: ", userId)
						));
	}

	public void deleteByID(String userId) {
		repo.deleteById(userId);
	}

	public Object genreateNewToken(String email, String password, String client_id, String client_secret, String grant_type) {
		User user = repo.findByEmailAndPassword(email, password);
		

		if(user != null) {
			try {
	            // Chuẩn bị dữ liệu form
	            String formData = "username=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
	                            + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8)
	                            + "&client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
	                            + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
	                            + "&grant_type=" + URLEncoder.encode(grant_type, StandardCharsets.UTF_8);
	            
	            // Tạo HttpClient
	            HttpClient client = HttpClient.newHttpClient();
	            
	            // Tạo HttpRequest
	            HttpRequest request = HttpRequest.newBuilder()
	                    .uri(new URI("http://localhost:9098/realms/microservices-sound_service-web-artist/protocol/openid-connect/token"))
	                    .header("Content-Type", "application/x-www-form-urlencoded")
	                    .POST(BodyPublishers.ofString(formData))
	                    .build();
	            
	            // Gửi yêu cầu và nhận phản hồi
	            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
	            
	            
	            // Xử lý phản hồi
	            System.out.println("Response status code: " + response.statusCode());
	            System.out.println("Response body: " + response.body());
	            
	            return response.body();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		return null;
	}
	
	public UserResponse login(String email, String password) {
		User user = repo.findByEmailAndPassword(email, password);

		if(user != null) {
			return mapper.toUserResponse(user);
		}
		
		return null;
	}

	public void logout(String client_id, String client_secret, String refresh_token) {
		try {
            // Chuẩn bị dữ liệu form
            String formData = "client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
                            + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
                            + "&refresh_token=" + URLEncoder.encode(refresh_token, StandardCharsets.UTF_8);
            
            // Tạo HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Tạo HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9098/realms/microservices-sound_service-web-artist/protocol/openid-connect/logout"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(formData))
                    .build();
            
            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            // Xử lý phản hồi
            System.out.println("Response status code: " + response.statusCode());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}


	public String introspectToken(String client_id, String client_secret, String token) {
		try {
            // Chuẩn bị dữ liệu form
            String formData = "client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
                            + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
                            + "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            
            // Tạo HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Tạo HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9098/realms/microservices-sound_service-web-artist/protocol/openid-connect/token/introspect"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(formData))
                    .build();
            
            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            // Xử lý phản hồi
            System.out.println("Response status code: " + response.body());
            System.out.println("Response status code: " + response.statusCode());
            
            return response.body();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	public Object refreshToken(String client_id, String client_secret, String refresh_token, String grant_type) {
		try {
            // Chuẩn bị dữ liệu form
            String formData = "client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
                            + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
                            + "&refresh_token=" + URLEncoder.encode(refresh_token, StandardCharsets.UTF_8)
            				+ "&grant_type=" + URLEncoder.encode(grant_type, StandardCharsets.UTF_8);
            
            // Tạo HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Tạo HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9098/realms/microservices-sound_service-web-artist/protocol/openid-connect/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(formData))
                    .build();
            
            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            // Xử lý phản hồi
            System.out.println("Response status code: " + response.body());
            System.out.println("Response status code: " + response.statusCode());
            
            return response.body();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	public String getTokenNoNeedLogin(String client_id, String client_secret, String grant_type) {
		try {
            // Chuẩn bị dữ liệu form
            String formData = 
                            "&client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
                            + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
                            + "&grant_type=" + URLEncoder.encode(grant_type, StandardCharsets.UTF_8);
            
            // Tạo HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Tạo HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9098/realms/microservices-sound_service-web-artist/protocol/openid-connect/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(formData))
                    .build();
            
            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            
            // Xử lý phản hồi
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            
            return response.body();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	public Object getLastAccessOfUser(String clientIdReal, String token) throws URISyntaxException, IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

        // Tạo HttpRequest với phương thức GET và header Authorization
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9098/admin/realms/microservices-sound_service-web-artist/clients/"+clientIdReal+"/user-sessions"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // Trả về phản hồi từ server
        return response.body();
	}

	
	

}
