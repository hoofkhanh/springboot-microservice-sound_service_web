package com.hokhanh.web.postedBeat;

import java.util.List;

public record ListPostedBeatResponse(
	List<PostedBeatResponse> postedBeats,
	long total
) {

}
