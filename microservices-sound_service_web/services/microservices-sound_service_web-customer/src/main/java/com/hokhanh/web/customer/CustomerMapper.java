package com.hokhanh.web.customer;

import org.springframework.stereotype.Service;

import com.hokhanh.web.user.UserResponse;

@Service
public class CustomerMapper {

	public Customer toCustomer(CustomerRequest request, String userId) {
		return Customer.builder()
				.userId(userId)
				.image(request.image())
				.build();
	}

	public CustomerResponse toCustomerResponse(Customer customer, UserResponse userResponse) {
		return new CustomerResponse(customer.getId(), customer.getImage(), userResponse);
	}

}
