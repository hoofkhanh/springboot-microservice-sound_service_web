package com.hokhanh.web.postedBeat;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostedBeatRepository extends JpaRepository<PostedBeat, Long> {

	List<PostedBeat> findByBeat_BeatNameContainingIgnoreCaseAndBeat_BeatGenre_Id(String beatName, Long id , Pageable pageable);
	
	List<PostedBeat> findByBeat_BeatNameContainingIgnoreCase(String beatName, Pageable pageable);
	
	List<PostedBeat> findBySellerId(String id);
	
	PostedBeat findByBeat_Id(Long beatId);

	long countByBeat_BeatNameContainingIgnoreCaseAndBeat_BeatGenre_Id( String beatName, Long beatGenreId);
	
	long countByBeat_BeatNameContainingIgnoreCase(String beatName);
}
