package com.hokhanh.web.artist;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hokhanh.web.common.Seller_Purchaser_Response;

@FeignClient(name = "artist-service", url = "${application.config.artist-url}")
public interface ArtistClient {

	@GetMapping("/{artist-id}")
	public Seller_Purchaser_Response findById(@PathVariable("artist-id") String artistId);
}
