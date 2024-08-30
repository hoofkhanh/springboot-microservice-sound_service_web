package com.hokhanh.web.artist;


import com.hokhanh.web.user.UserRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record ArtistRequest(
		
	String id,
	
	@NotBlank(message = "Artist artistName is required")
	String artistName,
	
//	@NotBlank(message = "Artist image is required")
//	String image,
	
	@NotBlank(message = "Artist introduction is required")
	String introduction,
	
	@NotBlank(message = "Artist nameOfIntroductionTrack is required")
	String nameOfIntroductionTrack,
	
//	@NotBlank(message = "Artist introductionTrackFile is required")
//	String introductionTrackFile,
	
	@NotNull(message = "Artist introductionTrackFile is required")
	ArtistType artistType,
	
	@Positive(message = "Artist hireCost should be positive")
	double hireCost,
	
	@NotNull(message = "Artist User is required")
	@Valid
	UserRequest user,
	
	@NotNull(message = "Artist's skills is required")
	@NotEmpty(message = "Artist's skills is not empty")
	List<Long> jobTypeIds
	
) {

}
