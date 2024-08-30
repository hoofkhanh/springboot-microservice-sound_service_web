package com.hokhanh.web.hire.soundChecker;

import java.time.LocalDate;

import com.hokhanh.web.common.Hirer_Expert_Response;

public record SoundCheckerHireResponse(
		
	Long id,
	Hirer_Expert_Response hirer,
	Hirer_Expert_Response expert,
	LocalDate startDate,
	LocalDate endDate,
	double price
	
	
) {

}
