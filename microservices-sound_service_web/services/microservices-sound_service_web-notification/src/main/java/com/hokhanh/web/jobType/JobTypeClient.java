package com.hokhanh.web.jobType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hokhanh.web.kafka.hire.jobType.JobTypeResponse;


@FeignClient(
		name = "job-type-service",
		url = "${application.config.job-url}"
)
public interface JobTypeClient {

	@GetMapping("/job-type/{job-type-id}")
	public JobTypeResponse findJobTypeById(@PathVariable("job-type-id") Long jobTypeId);
}
