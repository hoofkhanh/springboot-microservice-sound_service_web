package com.hokhanh.web.hire.soundMaker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SoundMakerHireRepository extends JpaRepository<SoundMakerHire, Long> {
	
	SoundMakerHire findByStartDateAndEndDateAndHirerIdAndExpertIdAndJobTypeId(LocalDate startDate, LocalDate endDate,
			String hirerId, String expertId, Long jobTypeId);
	
	List<SoundMakerHire> findByExpertIdAndEndDateGreaterThanEqual(String expertId, LocalDate now);
	
	List<SoundMakerHire> findByExpertIdAndEndDateLessThan(String expertId, LocalDate now);
 
	List<SoundMakerHire> findByHirerIdAndEndDateGreaterThanEqual(String hirerId, LocalDate now);
	
	List<SoundMakerHire> findByHirerIdAndEndDateLessThan(String hirerId, LocalDate now);
}
