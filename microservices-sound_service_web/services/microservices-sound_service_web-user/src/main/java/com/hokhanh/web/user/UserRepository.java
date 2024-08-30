package com.hokhanh.web.user;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	
	User findByPhoneNumber(String phoneNumber);

	User findByEmailAndPassword(String email, String password);
}
