package com.hokhanh.web.favorite.favoriteBeat;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.beat.BeatClient;
import com.hokhanh.web.common.Artist_Customer_Response;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.FavoriteException;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteBeatService {

	private final FavoriteBeatRepository repo;
	private final FavoriteBeatMapper mapper;
	
	private final CustomerClient customerClient;
	private final BeatClient beatClient;
	
	@Transactional
	public FavoriteBeatResponse addFavoriteBeat( FavoriteBeatRequest request) {
		if(repo.findByAccountOwnerIdAndPostedBeatId(request.accountOwnerId(), request.postedBeatId()) != null) {
			throw new FavoriteException("accountOwnerId and postedBeatId are existed in FavoriteBeat");  
		}
		
		Artist_Customer_Response accountOwnerResponse = null;
		try {
			accountOwnerResponse = customerClient.findById(request.accountOwnerId());
		} catch (FeignClientException e) {
			throw new FavoriteException("Not found Favorite Beat (accountOwner) customer with this accountOwnerId: "+ request.accountOwnerId());
		}
		
		var postedBeatResponse = beatClient.findById(request.postedBeatId());
		
		var favoriteBeat = repo.save(mapper.toFavoriteBeat(request));
		
		return mapper.toFavoriteBeatResponse(favoriteBeat, accountOwnerResponse, postedBeatResponse);
	}

	public void deleteFavoriteBeat(Long id) {
		repo.findById(id)
			.orElseThrow(() -> new FavoriteException("Not found Favorite Beat with this id: "+ id));
		
		repo.deleteById(id);
	}

	public List<FavoriteBeatResponse> findByAccountOwnerId(String accountOwnerId) {
		return repo.findByAccountOwnerId(accountOwnerId)
				.stream()
				.map(favoriteBeat -> {
					Artist_Customer_Response accountOwnerResponse = null;
					try {
						accountOwnerResponse = customerClient.findById(favoriteBeat.getAccountOwnerId());
					} catch (FeignClientException e) {
						throw new FavoriteException("Not found Favorite Beat (accountOwner) customer with this accountOwnerId: "+ favoriteBeat.getAccountOwnerId());
					}
					
					var postedBeatResponse = beatClient.findById(favoriteBeat.getPostedBeatId());
					
					return mapper.toFavoriteBeatResponse(favoriteBeat, accountOwnerResponse, postedBeatResponse);
				})
				.toList();
	}

	@Transactional
	public void deleteByPostedBeatId(Long postedBeatId) {
		repo.deleteByPostedBeatId(postedBeatId);
	}
}
