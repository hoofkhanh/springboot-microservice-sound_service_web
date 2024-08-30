package com.hokhanh.web.kafka.purchasedBeat.beat;


public record BeatResponse(
	Long id,
	BeatGenre beatGenre,
	String beatName,
	String beatFile
) {

}
