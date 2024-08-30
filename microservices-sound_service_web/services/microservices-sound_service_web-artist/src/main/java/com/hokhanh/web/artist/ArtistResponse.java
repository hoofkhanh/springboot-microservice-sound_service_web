package com.hokhanh.web.artist;

import java.util.List;

import com.hokhanh.web.skill.SkillResponse;
import com.hokhanh.web.user.UserResponse;

public record ArtistResponse(
	String id,
	String artistName,
	String image,
	String introduction,
	String nameOfIntroductionTrack,
	String introductionTrackFile,
	ArtistType artistType,
	double hireCost,
	UserResponse user,
	List<SkillResponse> skills
) {

}
