package com.hokhanh.web.favorite.favoriteBeat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FavoriteBeatRequest(
	Long id,
	
	@NotBlank(message = "Favorite Beat accountOwnerId is required")
	String accountOwnerId,
	
	@NotNull(message = "Favorite Beat postedBeatId is required")
	Long postedBeatId
) {

}
