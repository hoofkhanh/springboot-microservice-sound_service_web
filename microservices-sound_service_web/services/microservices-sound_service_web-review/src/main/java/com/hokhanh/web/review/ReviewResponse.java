package com.hokhanh.web.review;

import java.time.LocalDate;

import com.hokhanh.web.common.Artist_Customer_Response;

public record ReviewResponse(
	Long id,
	Artist_Customer_Response reviewer,
	Artist_Customer_Response expert,
	String content,
	LocalDate date
) {

}
