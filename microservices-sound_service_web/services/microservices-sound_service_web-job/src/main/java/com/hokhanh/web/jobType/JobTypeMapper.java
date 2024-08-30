package com.hokhanh.web.jobType;

import org.springframework.stereotype.Service;

@Service
public class JobTypeMapper {

	public JobTypeResponse toJobTypeResponse(JobType jobType) {
		return new JobTypeResponse(jobType.getId(), jobType.getName());
	}
}
