package com.hokhanh.web.beat;


public record BeatResponse(
	Long id,
	BeatGenre beatGenre,
	String beatName,
	String beatFile
) {

}
