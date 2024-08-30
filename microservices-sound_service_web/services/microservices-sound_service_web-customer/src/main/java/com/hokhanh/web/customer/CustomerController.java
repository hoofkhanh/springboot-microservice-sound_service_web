package com.hokhanh.web.customer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/customers")
@RequiredArgsConstructor
@RestController
public class CustomerController {

	private final CustomerService service;
	
	@PostMapping("/upload-image-file")
	public ResponseEntity<String> uploadImageFile(@RequestParam MultipartFile imageFile){
		return ResponseEntity.ok(service.uploadImageToFolder(imageFile));
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) throws URISyntaxException, IOException, InterruptedException{
		return ResponseEntity.ok(service.createCustomer(request));
	} 
	
	@PutMapping
	public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody @Valid CustomerRequest request){
		return ResponseEntity.ok(service.updateCustomer(request));
	} 
	
	@GetMapping("/{customer-id}")
	public ResponseEntity<CustomerResponse> findById(@PathVariable("customer-id") String customerId){
		return ResponseEntity.ok(service.findById(customerId));
	}
	
//	lấy hình ảnh trong folder ra
	@GetMapping("/images/{file}")
	public ResponseEntity<Resource> getImageFromFolder(@PathVariable("file") String file) throws MalformedURLException{
		Path path = Paths.get("src/main/resources/static/images/"+file);
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
	}
	
//	sau khi đăng nhập thì lấy thông tin customer theo userId
	@GetMapping("/find-by-user-id/{user-id}")
	public ResponseEntity<CustomerResponse> findByUserId(@PathVariable("user-id") String userId){
		return ResponseEntity.ok(service.findByUserId(userId));
	}
}
