package com.hokhanh.web.review;

import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(
	Long id,
	
	@NotBlank(message = "Review reviewerId is mandatory")
	String reviewerId,
	
	@NotBlank(message = "Review expertId is mandatory")
	String expertId,
	
	@NotBlank(message = "Review content is mandatory")
	String content
) {

}
