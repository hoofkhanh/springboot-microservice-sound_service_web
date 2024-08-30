package com.hokhanh.web.postedJob;

import java.time.LocalDate;

import com.hokhanh.web.common.PosterResponse;
import com.hokhanh.web.jobType.JobTypeResponse;

public record PostedJobResponse(
	Long id,
	PosterResponse poster,
	JobTypeResponse jobType,
	String topic,
	String content,
	String trackFile,
	LocalDate postDate
) {

}
