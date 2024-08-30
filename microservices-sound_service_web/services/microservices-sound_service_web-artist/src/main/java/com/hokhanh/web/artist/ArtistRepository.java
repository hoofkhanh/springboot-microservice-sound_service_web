package com.hokhanh.web.artist;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ArtistRepository extends MongoRepository<Artist, String> {

	List<Artist> findByArtistType(ArtistType artistType);
	
	List<Artist> findByArtistType(ArtistType artistType, Pageable pageable);

	long countByArtistType(ArtistType artistType);
	
	List<Artist> findByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType soundMaker, String artistName);
	
	List<Artist> findByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType soundMaker, String artistName, Pageable pageable);
	
	long countByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType soundMaker, String artistName);

	Artist findByUserId(String userId);

	
}
