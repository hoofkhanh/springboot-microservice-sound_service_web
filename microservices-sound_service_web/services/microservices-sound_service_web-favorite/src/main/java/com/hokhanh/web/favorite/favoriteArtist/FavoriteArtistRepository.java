package com.hokhanh.web.favorite.favoriteArtist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {

	List<FavoriteArtist> findByAccountOwnerId(String accountOwnerId);
	
	FavoriteArtist findByAccountOwnerIdAndArtistId(String accountOwnerId, String artistId);
}
