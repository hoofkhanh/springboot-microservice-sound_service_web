package com.hokhanh.web.jobType;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobTypeController {

	private final JobTypeService service;
	
	@GetMapping("/job-type/{job-type-id}")
	public ResponseEntity<JobTypeResponse> findById(@PathVariable("job-type-id") Long jobTypeId){
		return ResponseEntity.ok(service.findById(jobTypeId));
	}
	
	@GetMapping("/job-type")
	public ResponseEntity<List<JobTypeResponse>> findAll(){
		return ResponseEntity.ok(service.findAll());
	} 
	
}
