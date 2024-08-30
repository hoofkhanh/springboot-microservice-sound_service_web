package com.hokhanh.web.beat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
		name = "beat-service",
		url = "${application.config.beat-url}"
)
public interface BeatClient  {

	@GetMapping("/posted-beat/{posted-beat-id}")
	public PostedBeatResponse findById(@PathVariable("posted-beat-id") Long id);
}
