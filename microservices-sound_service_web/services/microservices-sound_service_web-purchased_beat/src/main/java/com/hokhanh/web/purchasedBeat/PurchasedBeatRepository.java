package com.hokhanh.web.purchasedBeat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedBeatRepository extends JpaRepository<PurchasedBeat, Long> {

	PurchasedBeat findByBeatId(Long beatId);
	
	List<PurchasedBeat> findByPurchaserId(String purchaserId);
}
