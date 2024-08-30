package com.hokhanh.web.artist;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "artists")
@Builder
public class Artist {

	@Id
	private String id;
	
	private String userId;
	
	private String artistName;
	
	private String image;
	private String introduction;
	private String nameOfIntroductionTrack;
	private String introductionTrackFile;
	private ArtistType artistType;
	private double hireCost;
}
