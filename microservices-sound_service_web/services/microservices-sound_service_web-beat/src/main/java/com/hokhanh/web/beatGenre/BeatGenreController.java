package com.hokhanh.web.beatGenre;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RequestMapping("/api/beats")
@RequiredArgsConstructor
@RestController
public class BeatGenreController {
	
	private final BeatGenreRepository repo;

	@GetMapping("/beat-genre")
	public ResponseEntity<List<BeatGenre>> findAll(){
		return ResponseEntity.ok(repo.findAll());
	}
}
