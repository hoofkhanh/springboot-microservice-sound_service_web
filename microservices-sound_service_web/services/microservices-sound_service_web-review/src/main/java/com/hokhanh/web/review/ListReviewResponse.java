package com.hokhanh.web.review;

import java.util.List;

public record ListReviewResponse(
	List<ReviewResponse> reviews,
	long total
) {

}
