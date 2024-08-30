package com.hokhanh.web.favoriteBeat;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = "favorite-service",
	url = "${application.config.favorite-url}"
)
public interface FavoriteBeatClient {

	@DeleteMapping("/favorite-beat/delete-by-posted-beat-id/{posted-beat-id}")
	public Void deleteByPostedBeatId(@PathVariable("posted-beat-id") Long postedBeatId);
}
