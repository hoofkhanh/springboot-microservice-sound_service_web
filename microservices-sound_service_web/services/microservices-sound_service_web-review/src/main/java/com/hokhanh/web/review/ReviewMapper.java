package com.hokhanh.web.review;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.Artist_Customer_Response;

@Service
public class ReviewMapper {

	public Review toReview(ReviewRequest request) {
		return Review.builder()
				.reviewerId(request.reviewerId())
				.expertId(request.expertId())
				.content(request.content())
				.date(LocalDate.now())
				.build();
	}

	public ReviewResponse toReviewResponse(Review review, Artist_Customer_Response reviewer,
			Artist_Customer_Response expert) {
		return new ReviewResponse(
				review.getId(), 
				reviewer, 
				expert, 
				review.getContent(),
				review.getDate()
				);
	}

}
