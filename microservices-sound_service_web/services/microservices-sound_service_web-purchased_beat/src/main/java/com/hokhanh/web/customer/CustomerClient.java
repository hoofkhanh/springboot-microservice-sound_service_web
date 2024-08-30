package com.hokhanh.web.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hokhanh.web.common.Seller_Purchaser_Response;

@FeignClient(
		name = "customer-service",
		url = "${application.config.customer-url}"
)
public interface CustomerClient {

	@GetMapping("/{customer-id}")
	public Seller_Purchaser_Response findById(@PathVariable("customer-id") String customerId);
}
