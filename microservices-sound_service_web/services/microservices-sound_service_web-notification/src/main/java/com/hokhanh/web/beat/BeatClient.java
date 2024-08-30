package com.hokhanh.web.beat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hokhanh.web.kafka.purchasedBeat.beat.BeatResponse;


@FeignClient(
		name = "beat-service",
		url = "${application.config.beat-url}"
)
public interface BeatClient {

	@GetMapping("/beat/{beat-id}")
	public BeatResponse findById(@PathVariable("beat-id") Long id);
}
