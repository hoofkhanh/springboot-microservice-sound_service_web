package com.hokhanh.web.postedBeat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.beat.BeatService;
import com.hokhanh.web.beatGenre.BeatGenreRepository;
import com.hokhanh.web.common.SellerResponse;
import com.hokhanh.web.exception.BeatGenreException;
import com.hokhanh.web.exception.PostedBeatException;
import com.hokhanh.web.favoriteBeat.FavoriteBeatClient;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostedBeatService {

	private final PostedBeatRepository repo;
	private final PostedBeatMapper mapper;
	
	private final BeatService beatService;
	
	private final ArtistClient artistClient;
	
	private final BeatGenreRepository beatGenreRepository;
	
	private final FavoriteBeatClient favoriteBeatClient;
	
	@Transactional
	public PostedBeatResponse createPostedBeat( PostedBeatRequest request) {
		var beatResponse = beatService.createBeat(request.beat());
		
		SellerResponse sellerResponse = null;
		try {
			sellerResponse = artistClient.findById(request.sellerId());
		} catch (FeignClientException e) {
			throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ request.sellerId());
		}
		
		var postedBeat = repo.save(mapper.toPostedBeat(request, beatResponse));
		
		return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
	}

	@Transactional
	public void deleteById(Long id) {
		var postedBeat = repo.findById(id)
				.orElseThrow( () -> new PostedBeatException("Not found postedBeat with this id: "+ id));
		
		repo.delete(postedBeat);
		
		beatService.deleteById(postedBeat.getBeat().getId());
		
//		xóa ở favorite nữa
		favoriteBeatClient.deleteByPostedBeatId(id);
	}

	public PostedBeatResponse findById(Long id) {
		var postedBeat = repo.findById(id)
				.orElseThrow( () -> new PostedBeatException("Not found postedBeat with this id: "+ id));
		
		var beatResponse = beatService.findById(postedBeat.getBeat().getId());
		
		SellerResponse sellerResponse = null;
		try {
			sellerResponse = artistClient.findById(postedBeat.getSellerId());
		} catch (FeignClientException e) {
			throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
		}
		
		return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
	}

	public List<PostedBeatResponse> findAll(int page) {
		var postedBeatResponses = repo.findAll(PageRequest.of(page, 3, Sort.by("id").descending()))
				.stream()
				.map(postedBeat -> {
					var beatResponse = beatService.findById(postedBeat.getBeat().getId());
					
					SellerResponse sellerResponse = null;
					try {
						sellerResponse = artistClient.findById(postedBeat.getSellerId());
					} catch (FeignClientException e) {
						throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
					}
					
					return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
				})
				.collect(Collectors.toList());
		
		return postedBeatResponses;
	}
	
	public long countAll() {
		return repo.count();
	}

	public List<PostedBeatResponse> findByBeatGenreAndBeatName(Long beatGenreId, String beatName, int page) {
		if(beatGenreId != 0 ) {
			beatGenreRepository.findById(beatGenreId)
				.orElseThrow(() -> new BeatGenreException("Not found BeatGenre with this id: "+ beatGenreId));
		}
		
		if(beatGenreId ==0) {
			return repo.findByBeat_BeatNameContainingIgnoreCase(beatName, PageRequest.of(page, 3, Sort.by("id").descending()))
					.stream()
					.map(postedBeat -> {
						var beatResponse = beatService.findById(postedBeat.getBeat().getId());
						
						SellerResponse sellerResponse = null;
						try {
							sellerResponse = artistClient.findById(postedBeat.getSellerId());
						} catch (FeignClientException e) {
							throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
						}
						
						return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
					})
					.collect(Collectors.toList());
		}
		
		var postedBeatResponses = repo.findByBeat_BeatNameContainingIgnoreCaseAndBeat_BeatGenre_Id(beatName, beatGenreId, PageRequest.of(page, 3,
				Sort.by("id").descending()))
				.stream()
				.sorted(Comparator.comparing(PostedBeat::getPostedDate).reversed())
				.map(postedBeat -> {
					var beatResponse = beatService.findById(postedBeat.getBeat().getId());
					
					SellerResponse sellerResponse = null;
					try {
						sellerResponse = artistClient.findById(postedBeat.getSellerId());
					} catch (FeignClientException e) {
						throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
					}
					
					return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
				})
				.collect(Collectors.toList());
		
		return postedBeatResponses;
	}
	
	public long countByBeatGenreAndBeatName(Long beatGenreId, String beatName) {
		if(beatGenreId ==0) {
			return repo.countByBeat_BeatNameContainingIgnoreCase(beatName);
		}else {
			return repo.countByBeat_BeatNameContainingIgnoreCaseAndBeat_BeatGenre_Id(beatName, beatGenreId);
		}
	}

	public List<PostedBeatResponse> findBySeller(String sellerId) {
		try {
			artistClient.findById(sellerId);
		} catch (FeignClientException e) {
			throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ sellerId);
		}
		
		var postedBeatResponses = repo.findBySellerId(sellerId)
				.stream()
				.sorted(Comparator.comparing(PostedBeat::getPostedDate).reversed())
				.map(postedBeat -> {
					var beatResponse = beatService.findById(postedBeat.getBeat().getId());
					
					SellerResponse sellerResponse = null;
					try {
						sellerResponse = artistClient.findById(postedBeat.getSellerId());
					} catch (FeignClientException e) {
						throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
					}
					
					return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
				})
				.collect(Collectors.toList());
		
		return postedBeatResponses;
	}

	public void deleteByBuyingBeat(Long id) {
		var postedBeat = repo.findById(id)
				.orElseThrow( () -> new PostedBeatException("Not found postedBeat with this id: "+ id));
		
		repo.delete(postedBeat);
		
//		xóa ở favorite nữa
		favoriteBeatClient.deleteByPostedBeatId(id);
	}

	public PostedBeatResponse findByBeatId(Long beatId) {
		var postedBeat = repo.findByBeat_Id(beatId);
		if(postedBeat == null) {
			throw new PostedBeatException("Not found postedBeat with this beatId: "+ beatId);
		}
		
		var beatResponse = beatService.findById(postedBeat.getBeat().getId());
		
		SellerResponse sellerResponse = null;
		try {
			sellerResponse = artistClient.findById(postedBeat.getSellerId());
		} catch (FeignClientException e) {
			throw new PostedBeatException("Not found PostedJob (seller) artist with this id: "+ postedBeat.getSellerId());
		}
		
		return mapper.toPostedBeatResponse(postedBeat, sellerResponse, beatResponse);
	}

	
}
