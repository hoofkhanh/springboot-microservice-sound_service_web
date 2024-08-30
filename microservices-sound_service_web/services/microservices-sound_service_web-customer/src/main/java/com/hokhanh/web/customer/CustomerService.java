package com.hokhanh.web.customer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.web.exception.CustomerNotFoundException;
import com.hokhanh.web.exception.CustomerNullIdException;
import com.hokhanh.web.user.UserClient;
import com.hokhanh.web.util.UploadFileToFolder;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repo;
	private final CustomerMapper mapper;
	private final UploadFileToFolder toFolder;
	private final UserClient userClient;
	
	public String uploadImageToFolder(MultipartFile imageFile) {
		return toFolder.uploadFileOfImage(imageFile);
	}

	@Transactional
	public CustomerResponse createCustomer( CustomerRequest request) throws URISyntaxException, IOException, InterruptedException {
		var userResponse = userClient.createUser(request.user());
		
		var customer = repo.save(mapper.toCustomer(request, userResponse.id()));
		
		JSONObject credentials = new JSONObject();
        credentials.put("temporary", false);
        credentials.put("type", "password");
        credentials.put("value", userResponse.password());

        // Tạo JSON Array và thêm "credentials"
        JSONArray credentialsArray = new JSONArray();
        credentialsArray.add(credentials);

        // Tạo JSON cho toàn bộ dữ liệu
        JSONObject json = new JSONObject();
        json.put("credentials", credentialsArray);
        json.put("username", userResponse.email());
        json.put("firstName", userResponse.fullName().split(" ")[userResponse.fullName().split(" ").length - 1]);
        json.put("lastName", userResponse.fullName().split(" ")[0]);
        json.put("email", userResponse.email());
        json.put("emailVerified", false);
        json.put("enabled", true);
        
        
        HttpClient client = HttpClient.newHttpClient();

        // Tạo HttpRequest
        HttpRequest requestUR = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9098/admin/realms/microservices-sound_service-web-artist/users"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + request.token())
                .POST(BodyPublishers.ofString(json.toString()))
                .build();

        // Gửi yêu cầu và nhận phản hồi
        HttpResponse<String> response = client.send(requestUR, HttpResponse.BodyHandlers.ofString());
        
        
        // Xử lý phản hồi
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
		return mapper.toCustomerResponse(customer, userResponse);
	}

	@Transactional
	public CustomerResponse updateCustomer( CustomerRequest request) {
		if(request.id() == null ) {
			throw new CustomerNullIdException("Need the ID to update customer");
		}
		var customer = repo.findById(request.id())
				.orElseThrow(() -> new CustomerNotFoundException("Can't update customer with this wrong ID: "+request.id()));
		
		if(customer.getUserId().equals(request.user().id()) == false) {
			throw new CustomerNotFoundException("Can't update customer because userId of customer is incorrect");
		}
		
		var userResponse = userClient.updateUser(request.user());
		
		if(request.image() != null) {
			customer.setImage(request.image());
		}
		
		return mapper.toCustomerResponse(repo.save(customer), userResponse);
	}

	public CustomerResponse findById(String customerId) {
		var customer = repo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Can't findById of customer with this wrong ID: "+ customerId));
		
		var userResponse = userClient.findUserById(customer.getUserId());
		return mapper.toCustomerResponse(customer, userResponse);
	}

	public CustomerResponse findByUserId(String userId) {
		Customer customer = repo.findByUserId(userId);
		
		if(customer != null ) {
			 userClient.findUserById(customer.getUserId());
			 var userResponse = userClient.findUserById(customer.getUserId());
			 return mapper.toCustomerResponse(customer, userResponse);
		}else {
			return null;
		}
	}
}
