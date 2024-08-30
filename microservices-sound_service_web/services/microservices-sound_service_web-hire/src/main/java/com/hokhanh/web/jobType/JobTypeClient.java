package com.hokhanh.web.jobType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient( name = "job-type-service", url = "${application.config.job-type-url}")
public interface JobTypeClient {

	@GetMapping("/job-type/{job-type-id}")
	public JobTypeResponse findById(@PathVariable("job-type-id") Long jobTypeId);
}
