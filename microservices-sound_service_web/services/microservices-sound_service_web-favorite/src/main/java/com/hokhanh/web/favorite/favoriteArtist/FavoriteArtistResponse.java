package com.hokhanh.web.favorite.favoriteArtist;

import com.hokhanh.web.common.Artist_Customer_Response;

public record FavoriteArtistResponse(
	Long id,
	Artist_Customer_Response accountOwner,
	Artist_Customer_Response artist
) {

}
