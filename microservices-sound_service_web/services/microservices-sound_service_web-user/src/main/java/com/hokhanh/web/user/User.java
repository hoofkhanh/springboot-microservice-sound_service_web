package com.hokhanh.web.user;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

	@Id
	private String id;
	
	private String email;
	private String password;
	private String fullName;
	private String phoneNumber;
	private String country;
	private LocalDate dayOfBirth;
	private Role role;
}
