package com.hokhanh.web.review;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

	private final ReviewService service;
	
	@PostMapping
	public ResponseEntity<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest request) {
		return ResponseEntity.ok(service.addReview(request));
	}
	
//	không xóa cmt được
//	@DeleteMapping("{review-id}")
//	public ResponseEntity<Void> deleteReview(@PathVariable("review-id") Long reviewId) {
//		service.deleteReview(reviewId);
//		return ResponseEntity.accepted().build();
//	}
	
	@GetMapping("/find-by-expert-id/{expert-id}")
	public ResponseEntity<ListReviewResponse> findByExpertId(@PathVariable("expert-id") String expertId, 
			@RequestParam(defaultValue = "-1") int page){
		return ResponseEntity.ok(new ListReviewResponse(service.findByExpertId(expertId, page), service.countByExpertId(expertId)));
	}
	
	@GetMapping("/find-by-expert-id-in-sound-checker-page/{expert-id}")
	public ResponseEntity<ListReviewResponse> findByExpertIdInSoundCheckerPage(@PathVariable("expert-id") String expertId){
		return ResponseEntity.ok(new ListReviewResponse(service.findByExpertIdInSoundCheckerPage(expertId), service.countByExpertId(expertId)));
	}
	
}
