package com.hokhanh.web.review;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByExpertIdOrderByIdDesc(String expertId, Pageable pageable);
	
	long countByExpertId(String expertId);
	
	List<Review> findByExpertIdOrderByIdDesc(String expertId);

}
