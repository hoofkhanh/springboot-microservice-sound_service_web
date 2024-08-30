package com.hokhanh.web.favorite.favoriteBeat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteBeatRepository extends JpaRepository<FavoriteBeat, Long> {

	FavoriteBeat findByAccountOwnerIdAndPostedBeatId(String accountOwnerId, Long postedBeatId);

	List<FavoriteBeat> findByAccountOwnerId(String accountOwnerId);

	void deleteByPostedBeatId(Long postedBeatId);

}
