package com.hokhanh.web.favorite.favoriteArtist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.common.Artist_Customer_Response;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.FavoriteException;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteArtistService {

	private final FavoriteArtistRepository repo;
	private final FavoriteArtistMapper mapper;
	
	private final CustomerClient customerClient;
	private final ArtistClient artistClient;
	
	@Transactional
	public FavoriteArtistResponse addFavoriteArtist( FavoriteArtistRequest request) {
		if(repo.findByAccountOwnerIdAndArtistId(request.accountOwnerId(), request.artistId()) != null) {
			throw new FavoriteException("accountOwnerId and artistId are existed in FavoriteArtist"); 
		}
		
		Artist_Customer_Response accountOwner = null;
		try {
			accountOwner = customerClient.findById(request.accountOwnerId());
		} catch (FeignClientException e) {
			throw new FavoriteException("Not found Favorite Artist (accountOwner) customer with this accountOwnerId: "+ request.accountOwnerId());
		}
		
		Artist_Customer_Response artist = null;
		try {
			artist = artistClient.findById(request.artistId());
		} catch (FeignClientException e) {
			throw new FavoriteException("Not found Favorite Artist (artist) with this artistId: "+ request.artistId());
		}
		
		var favoriteArtist = repo.save(mapper.toFavoriteArtist(request));
		
		return mapper.toFavoriteArtistResponse(favoriteArtist, accountOwner, artist);
	}

	
	public void deleteFavoriteArtist(Long id) {
		repo.findById(id)
			.orElseThrow(() -> new FavoriteException("Not found Favorite Artist with this id: "+ id));
		
		repo.deleteById(id);
	}

	public List<FavoriteArtistResponse> findByAccountOwnerId(String accountOwnerId) {
		return repo.findByAccountOwnerId(accountOwnerId)
				.stream()
				.map(favoriteArtist -> {
					Artist_Customer_Response accountOwner = null;
					try {
						accountOwner = customerClient.findById(favoriteArtist.getAccountOwnerId());
					} catch (FeignClientException e) {
						throw new FavoriteException("Not found Favorite (accountOwner) customer with this accountOwnerId: "+ favoriteArtist.getAccountOwnerId());
					}
					
					Artist_Customer_Response artist = null;
					try {
						artist = artistClient.findById(favoriteArtist.getArtistId());
					} catch (FeignClientException e) {
						throw new FavoriteException("Not found Favorite artist with this artistId: "+ favoriteArtist.getArtistId());
					}
					
					return mapper.toFavoriteArtistResponse(favoriteArtist, accountOwner, artist);
				})
				.collect(Collectors.toList());
	}
}
