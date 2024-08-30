package com.hokhanh.web.artist;

import java.util.List;

public record ListArtistResponse(
	List<ArtistResponse> artists,
	long total
) {

}
