package com.hokhanh.web.favorite.favoriteArtist;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteArtistController {

	private final FavoriteArtistService service;
	
	@PostMapping("/favorite-artist")
	public ResponseEntity<FavoriteArtistResponse> addFavoriteArtist(@RequestBody @Valid FavoriteArtistRequest request){
		return ResponseEntity.ok(service.addFavoriteArtist(request));
	}
	
	@DeleteMapping("/favorite-artist/{favorite-artist-id}")
	public ResponseEntity<Void> deleteFavoriteArtist(@PathVariable("favorite-artist-id") Long id){
		service.deleteFavoriteArtist(id);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/favorite-artist/find-by-account-owner-id/{account-owner-id}")
	public ResponseEntity<List<FavoriteArtistResponse>> findByAccountOwnerId(@PathVariable("account-owner-id") String accountOwnerId){
		return ResponseEntity.ok(service.findByAccountOwnerId(accountOwnerId));
	}
}
