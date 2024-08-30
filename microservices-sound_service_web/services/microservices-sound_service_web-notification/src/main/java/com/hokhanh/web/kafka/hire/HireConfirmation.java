package com.hokhanh.web.kafka.hire;

import java.time.LocalDate;

import com.hokhanh.web.kafka.hire.jobType.JobTypeResponse;
import com.hokhanh.web.kafka.purchasedBeat.user.Seller_Purchaser_Response;


public record HireConfirmation(
	Seller_Purchaser_Response hirer,
	Seller_Purchaser_Response expert,
	JobTypeResponse jobType,
	LocalDate startDate,
	LocalDate endDate,
	double price
) {

}
