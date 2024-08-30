package com.hokhanh.web.favorite.favoriteArtist;

import jakarta.validation.constraints.NotBlank;

public record FavoriteArtistRequest(
	Long id,
	
	@NotBlank(message = "Favorite Artist accountOwnerId is required")
	String accountOwnerId,
	
	@NotBlank(message = "Favorite Artist artistId is required")
	String artistId
) {

}
