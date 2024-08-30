package com.hokhanh.web.hire;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hokhanh.web.hire.soundChecker.SoundCheckerHireRequest;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireResponse;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRequest;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireResponse;




@FeignClient(
		name = "hire-service",
		url = "${application.config.hire-url}"
)
public interface HireClient {

	@PostMapping("/save-sound-maker-hire")
	public SoundMakerHireResponse saveSoundMakerHire(@RequestBody SoundMakerHireRequest request) ;
	
	@PostMapping("/save-sound-checker-hire")
	public SoundCheckerHireResponse saveSoundCheckerHire(@RequestBody SoundCheckerHireRequest request);
}
