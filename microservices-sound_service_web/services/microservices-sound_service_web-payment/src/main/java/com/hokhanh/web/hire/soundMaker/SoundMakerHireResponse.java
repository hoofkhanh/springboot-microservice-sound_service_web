package com.hokhanh.web.hire.soundMaker;

import java.time.LocalDate;

import com.hokhanh.web.hire.common.Hirer_Expert_Response;
import com.hokhanh.web.hire.soundMaker.jobType.JobTypeResponse;


public record SoundMakerHireResponse(
		
	Long id,
	Hirer_Expert_Response hirer,
	Hirer_Expert_Response expert,
	JobTypeResponse jobType,
	LocalDate startDate,
	LocalDate endDate,
	double price
	
	
) {

}
