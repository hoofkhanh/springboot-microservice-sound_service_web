package com.hokhanh.web.common;

import java.util.List;

import com.hokhanh.web.artist.ArtistType;
import com.hokhanh.web.artist.SkillResponse;
import com.hokhanh.web.user.UserResponse;

public record Hirer_Expert_Response(
	String id,
	double hireCost,
	UserResponse user,
	ArtistType artistType,
	String artistName,
	List<SkillResponse> skills
) {

}
