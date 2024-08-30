package com.hokhanh.web.customer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

	Customer findByUserId(String userId);

}
