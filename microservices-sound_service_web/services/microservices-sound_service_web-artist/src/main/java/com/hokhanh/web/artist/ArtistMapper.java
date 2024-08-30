package com.hokhanh.web.artist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hokhanh.web.skill.SkillResponse;
import com.hokhanh.web.user.UserResponse;

@Service
public class ArtistMapper {

	public Artist toArtist(ArtistRequest request, String userId, String pathOfImage, String pathOfTrack) {
		return Artist.builder()
				.id(request.id())
				.userId(userId)
				.artistName(request.artistName())
				.image(pathOfImage)
				.introduction(request.introduction())
				.nameOfIntroductionTrack(request.nameOfIntroductionTrack())
				.introductionTrackFile(pathOfTrack)
				.artistType(request.artistType())
				.hireCost(request.hireCost())
				.build();
	}

	public ArtistResponse toArtistResponse(Artist artist, UserResponse userResponse, List<SkillResponse> skillResponseList) {
		return new ArtistResponse(
				artist.getId(), 
				artist.getArtistName(), 
				artist.getImage(), 
				artist.getIntroduction(), 
				artist.getNameOfIntroductionTrack(), 
				artist.getIntroductionTrackFile(), 
				artist.getArtistType(), 
				artist.getHireCost(),
				userResponse,
				skillResponseList
			);
				
	}

}
