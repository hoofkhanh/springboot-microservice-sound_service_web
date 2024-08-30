package com.hokhanh.web.hire.soundMaker;

import java.time.LocalDate;


public record SoundMakerHireRequest(
	
	Long id,
	String hirerId,
	String expertId,
	Long jobTypeId,
	LocalDate startDate,
	LocalDate endDate
) {

}
