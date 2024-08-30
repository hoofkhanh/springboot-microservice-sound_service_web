package com.hokhanh.web.postedJob;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostedJobRequest(
	Long id,
	
	@NotBlank(message = "PostedJob posterId is required")
	String posterId,
	
	@NotNull(message = "PostedJob jobTypeId is required")
	Long jobTypeId,
	
	@NotBlank(message = "PostedJob topic is required")
	String topic,
	
	@NotBlank(message = "PostedJob content is required")
	String content,
	
	String trackFile
) {

}
