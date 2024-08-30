package com.hokhanh.web.hire.soundChecker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SoundCheckerRepository extends JpaRepository<SoundCheckerHire, Long> {

	SoundCheckerHire findByStartDateAndEndDateAndHirerIdAndExpertId(LocalDate startDate, LocalDate endDate,
			String hirerId, String expertId);

	List<SoundCheckerHire> findByExpertIdAndEndDateGreaterThanEqual(String soundCheckerId, LocalDate now);
	
	List<SoundCheckerHire> findByExpertIdAndEndDateLessThan(String soundCheckerId, LocalDate now);
	
	List<SoundCheckerHire> findByHirerIdAndEndDateGreaterThanEqual(String hirerId, LocalDate now);
	
	List<SoundCheckerHire> findByHirerIdAndEndDateLessThan(String hirerId, LocalDate now);
}
