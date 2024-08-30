package com.hokhanh.web.favorite.favoriteArtist;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.Artist_Customer_Response;


@Service
public class FavoriteArtistMapper {

	public FavoriteArtist toFavoriteArtist(FavoriteArtistRequest request) {
		return FavoriteArtist.builder()
				.accountOwnerId(request.accountOwnerId())
				.artistId(request.artistId())
				.build();
	}

	public FavoriteArtistResponse toFavoriteArtistResponse(FavoriteArtist favoriteArtist,
			Artist_Customer_Response accountOwner, Artist_Customer_Response artist) {
		return new FavoriteArtistResponse(
				favoriteArtist.getId(), 
				accountOwner, 
				artist
				);
	}

	

}
