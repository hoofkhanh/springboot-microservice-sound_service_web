package com.hokhanh.web.postedBeat;


import com.hokhanh.web.beat.BeatRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostedBeatRequest(
	Long id,
	
	@NotNull(message = "PostedBeat beat is required")
	@Valid
	BeatRequest beat,
	
	@NotBlank(message = "PostedBeat sellerId is required")
	String sellerId,
	
	@NotNull(message = "PostedBeat price is required")
	@Positive(message = "PostedBeat price must be greater than 0")
	double price
) {

}
