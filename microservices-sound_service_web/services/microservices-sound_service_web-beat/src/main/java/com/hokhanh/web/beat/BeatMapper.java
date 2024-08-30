package com.hokhanh.web.beat;

import org.springframework.stereotype.Service;

import com.hokhanh.web.beatGenre.BeatGenre;


@Service
public class BeatMapper {

	public Beat toBeat(BeatRequest request, BeatGenre beatGenre) {
		return Beat.builder()
				.beatGenre(beatGenre)
				.beatName(request.beatName())
				.beatFile(request.beatFile())
				.build();
	}

	public BeatResponse toBeatResponse(Beat beat) {
		return new BeatResponse(
				beat.getId(), 
				beat.getBeatGenre(), 
				beat.getBeatName(), 
				beat.getBeatFile()
				);
	}

	
}
