package com.hokhanh.web.postedJob;

import java.util.List;

public record ListPostedJobResponse(
	List<PostedJobResponse> postedJobs,
	long total
) {

}
