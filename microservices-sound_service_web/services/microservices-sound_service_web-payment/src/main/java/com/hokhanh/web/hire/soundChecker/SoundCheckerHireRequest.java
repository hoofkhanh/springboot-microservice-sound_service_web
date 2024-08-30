package com.hokhanh.web.hire.soundChecker;

import java.time.LocalDate;


public record SoundCheckerHireRequest(
	
	Long id,
	String hirerId,
	String expertId,
	LocalDate startDate,
	LocalDate endDate
) {

}
