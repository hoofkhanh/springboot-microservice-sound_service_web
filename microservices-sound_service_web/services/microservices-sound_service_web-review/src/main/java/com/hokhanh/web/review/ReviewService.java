package com.hokhanh.web.review;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.common.Artist_Customer_Response;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.ReviewException;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository repo;
	private final ReviewMapper mapper;
	
	private final CustomerClient customerClient;
	private final ArtistClient artistClient;
	
	@Transactional
	public ReviewResponse addReview( ReviewRequest request) {
		Artist_Customer_Response reviewer = null;
		try {
			reviewer = customerClient.findById(request.reviewerId());
		} catch (FeignClientException e) {
			throw new ReviewException("Not found Review (reviewer) customer with this reviewerId: "+ request.reviewerId());
		}
		
		Artist_Customer_Response expert = null;
		try {
			expert = artistClient.findById(request.expertId());
		} catch (FeignClientException e) {
			throw new ReviewException("Not found Review (expert) artist with this expertId: "+ request.expertId());
		}
		
		var review = repo.save(mapper.toReview(request));
		
		return mapper.toReviewResponse(review, reviewer, expert);
	}

	public void deleteReview(Long reviewId) {
		repo.findById(reviewId)
			.orElseThrow(() -> new ReviewException("Not found Review with this id: "+ reviewId));
		
		repo.deleteById(reviewId);
	}

	public List<ReviewResponse> findByExpertId(String expertId, int page) {
		if(page == -1) {
			return null;
		}
		
		return repo.findByExpertIdOrderByIdDesc(expertId, PageRequest.of(page, 1))
				.stream()
				.map(review -> {
					Artist_Customer_Response reviewer = null;
					try {
						reviewer = customerClient.findById(review.getReviewerId());
					} catch (FeignClientException e) {
						throw new ReviewException("Not found Review (reviewer) customer with this reviewerId: "+ review.getReviewerId());
					}
					
					Artist_Customer_Response expert = null;
					try {
						expert = artistClient.findById(review.getExpertId());
					} catch (FeignClientException e) {
						throw new ReviewException("Not found Review (expert) artist with this expertId: "+ review.getExpertId());
					}
					
					return mapper.toReviewResponse(review, reviewer, expert);
				})
				.toList();
	}
	
	public long countByExpertId(String expertId) {
		return repo.countByExpertId(expertId);
	}
	
	public List<ReviewResponse> findByExpertIdInSoundCheckerPage(String expertId) {
		return repo.findByExpertIdOrderByIdDesc(expertId)
				.stream()
				.map(review -> {
					Artist_Customer_Response reviewer = null;
					try {
						reviewer = customerClient.findById(review.getReviewerId());
					} catch (FeignClientException e) {
						throw new ReviewException("Not found Review (reviewer) customer with this reviewerId: "+ review.getReviewerId());
					}
					
					Artist_Customer_Response expert = null;
					try {
						expert = artistClient.findById(review.getExpertId());
					} catch (FeignClientException e) {
						throw new ReviewException("Not found Review (expert) artist with this expertId: "+ review.getExpertId());
					}
					
					return mapper.toReviewResponse(review, reviewer, expert);
				})
				.toList();
	}
}
