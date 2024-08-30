package com.hokhanh.web.beat;

import com.hokhanh.web.beatGenre.BeatGenre;

public record BeatResponse(
	Long id,
	BeatGenre beatGenre,
	String beatName,
	String beatFile
) {

}
