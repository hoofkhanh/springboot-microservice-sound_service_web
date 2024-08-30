package com.hokhanh.web.postedJob;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.PosterResponse;
import com.hokhanh.web.jobType.JobType;
import com.hokhanh.web.jobType.JobTypeResponse;

@Service
public class PostedJobMapper {

	public PostedJob toPostedJob(PostedJobRequest request) {
		return PostedJob.builder()
				.posterId(request.posterId())
				.jobType(
						JobType.builder()
						.id(request.jobTypeId())
						.build()
						)
				.topic(request.topic())
				.content(request.content())
				.trackFile(request.trackFile())
				.postDate(LocalDate.now())
				.build();
	}

	public PostedJobResponse toPostedJobResponse(PostedJob postedJob, JobTypeResponse jobTypeResponse,
			PosterResponse posterResponse) {
		return new PostedJobResponse(
					postedJob.getId(), 
					posterResponse, 
					jobTypeResponse, 
					postedJob.getTopic(), 
					postedJob.getContent(), 
					postedJob.getTrackFile(),
					postedJob.getPostDate()
				);
	}

	

}
