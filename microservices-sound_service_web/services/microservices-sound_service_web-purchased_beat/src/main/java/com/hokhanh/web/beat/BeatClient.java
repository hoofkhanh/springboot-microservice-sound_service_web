package com.hokhanh.web.beat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
		name = "beat-service",
		url = "${application.config.beat-url}"
)
public interface BeatClient {

	@GetMapping("/posted-beat/find-by-beat-id/{beat-id}")
	PostedBeatResponse findByBeatId(@PathVariable("beat-id") Long beatId);
	
	@DeleteMapping("/posted-beat/delete-by-purchasing-beat/{posted-beat-id}")
	void deleteByBuyingBeat(@PathVariable("posted-beat-id") Long id);
	
	@GetMapping("/beat/{beat-id}")
	public BeatResponse findById(@PathVariable("beat-id") Long id);
}
