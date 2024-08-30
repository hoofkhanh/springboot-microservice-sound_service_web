package com.hokhanh.web.favorite.favoriteBeat;

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
public class FavoriteBeatController {

	private final FavoriteBeatService service;
	
	@PostMapping("/favorite-beat")
	public ResponseEntity<FavoriteBeatResponse> addFavoriteBeat(@RequestBody @Valid FavoriteBeatRequest request){
		return ResponseEntity.ok(service.addFavoriteBeat(request));
	}
	
	@DeleteMapping("/favorite-beat/{favorite-beat-id}")
	public ResponseEntity<Void> deleteFavoriteBeat(@PathVariable("favorite-beat-id") Long id){
		service.deleteFavoriteBeat(id);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/favorite-beat/find-by-account-owner-id/{account-owner-id}")
	public ResponseEntity<List<FavoriteBeatResponse>> findByAccountOwnerId(@PathVariable("account-owner-id") String accountOwnerId){
		return ResponseEntity.ok(service.findByAccountOwnerId(accountOwnerId));
	}
	
//	api được call tới để sau khi beat được đăng bị xóa do seller xóa hoặc đc mua thì sẽ xóa trong favorite beat luôn
	@DeleteMapping("/favorite-beat/delete-by-posted-beat-id/{posted-beat-id}")
	public ResponseEntity<Void> deleteByPostedBeatId(@PathVariable("posted-beat-id") Long postedBeatId){
		service.deleteByPostedBeatId(postedBeatId);
		return ResponseEntity.accepted().build();
	}
}
