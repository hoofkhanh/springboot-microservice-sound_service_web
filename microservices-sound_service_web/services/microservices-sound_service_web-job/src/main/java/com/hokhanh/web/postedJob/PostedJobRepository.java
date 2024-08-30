package com.hokhanh.web.postedJob;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.web.jobType.JobType;

public interface PostedJobRepository extends JpaRepository<PostedJob, Long> {
	
	List<PostedJob> findByJobType(JobType jobType, Pageable pageable);
	
	long countByJobType_Id(Long jobType);

	List<PostedJob> findByPosterIdOrderByIdDesc(String posterId);
}
