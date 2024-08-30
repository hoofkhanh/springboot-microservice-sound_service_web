package com.hokhanh.web.hire.soundMaker;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SoundMakerHireRequest(
	
	Long id,
	
	@NotBlank(message = "Hire hireId is required")
	String hirerId,
	
	@NotBlank(message = "Hire expertId is required")
	String expertId,
	
	@NotNull(message = "Hire jobTypeId is required")
	Long jobTypeId,
	
	@NotNull(message = "Hire startDate is required")
	LocalDate startDate,
	
	@NotNull(message = "Hire endDate is required")
	LocalDate endDate
) {

}
