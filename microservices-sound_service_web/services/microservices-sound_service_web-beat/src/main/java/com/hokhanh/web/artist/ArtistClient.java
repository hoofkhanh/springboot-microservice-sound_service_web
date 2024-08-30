package com.hokhanh.web.artist;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hokhanh.web.common.SellerResponse;

@FeignClient(name = "artist-service", url = "${application.config.artist-url}")
public interface ArtistClient {

	@GetMapping("/{artist-id}")
	public SellerResponse findById(@PathVariable("artist-id") String artistId);
}
