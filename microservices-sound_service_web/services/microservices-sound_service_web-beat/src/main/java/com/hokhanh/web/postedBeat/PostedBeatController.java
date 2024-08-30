package com.hokhanh.web.postedBeat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/beats")
@RequiredArgsConstructor
@RestController
public class PostedBeatController {
	private final PostedBeatService service;
	
	@PostMapping("/posted-beat")
	public ResponseEntity<PostedBeatResponse> createPostedBeat(@RequestBody @Valid PostedBeatRequest request) {
		return ResponseEntity.ok(service.createPostedBeat(request));
	}
	
//	tự mình bấm xóa
	@DeleteMapping("/posted-beat/{posted-beat-id}")
	public ResponseEntity<Void> deleteById(@PathVariable("posted-beat-id") Long id) {
		service.deleteById(id);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/posted-beat/{posted-beat-id}")
	public ResponseEntity<PostedBeatResponse> findById(@PathVariable("posted-beat-id") Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping("/posted-beat")
	public ResponseEntity<ListPostedBeatResponse> findAll(@RequestParam int page) {
		return ResponseEntity.ok(new ListPostedBeatResponse(service.findAll(page), service.countAll()));
	}
	
	@GetMapping("/posted-beat/find-by-beat-genre-id-and-beat-name")
	public ResponseEntity<ListPostedBeatResponse> findByBeatGenreAndBeatName(@RequestParam("beat-genre-id") Long beatGenreId,
			@RequestParam("beat-name") String beatName, @RequestParam int page) {
		return ResponseEntity.ok(new ListPostedBeatResponse(service.findByBeatGenreAndBeatName(beatGenreId, beatName, page), 
				service.countByBeatGenreAndBeatName(beatGenreId, beatName)));
	}
	
	@GetMapping("/posted-beat/find-by-seller-id/{seller-id}")
	public ResponseEntity<List<PostedBeatResponse>> findBySeller(@PathVariable("seller-id") String sellerId) {
		return ResponseEntity.ok(service.findBySeller(sellerId));
	}
	
//	để check xong rồi mua beat đc call ở bên chỗ khác
	@GetMapping("/posted-beat/find-by-beat-id/{beat-id}")
	public ResponseEntity<PostedBeatResponse> findByBeatId(@PathVariable("beat-id") Long beatId) {
		return ResponseEntity.ok(service.findByBeatId(beatId));
	}
	
//	đc call ở bên chỗ khác
	@DeleteMapping("/posted-beat/delete-by-purchasing-beat/{posted-beat-id}")
	public ResponseEntity<Void> deleteByBuyingBeat(@PathVariable("posted-beat-id") Long id) {
		service.deleteByBuyingBeat(id);
		return ResponseEntity.accepted().build();
	}
}
