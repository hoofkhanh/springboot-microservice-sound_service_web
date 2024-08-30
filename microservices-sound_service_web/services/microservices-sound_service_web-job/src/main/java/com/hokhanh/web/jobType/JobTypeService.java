package com.hokhanh.web.jobType;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.hokhanh.web.exception.JobTypeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class JobTypeService {

	private final JobTypeRepository repo;
	private final JobTypeMapper mapper;

	public JobTypeResponse findById(Long jobTypeId) {
		return repo.findById(jobTypeId)
				.map(jobType -> mapper.toJobTypeResponse(jobType))
				.orElseThrow(() -> new JobTypeException("No Job Type with this id: " + jobTypeId));
	}

	public List<JobTypeResponse> findAll() {
		return repo.findAll()
				.stream()
				.map(jobType -> mapper.toJobTypeResponse(jobType))
				.toList();
	}

}
