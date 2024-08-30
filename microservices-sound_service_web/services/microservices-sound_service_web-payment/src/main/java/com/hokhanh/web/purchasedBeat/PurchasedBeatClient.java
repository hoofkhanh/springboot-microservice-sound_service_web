package com.hokhanh.web.purchasedBeat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
		name = "purchased-beat-service",
		url = "${application.config.purchased-beat-url}"
)
public interface PurchasedBeatClient {

	@GetMapping("/temporary-purchased-beat/{temporary-purchased-beat-id}")
	public TemporaryPurchasedBeat findTemporaryPurchasedBeatById(@PathVariable("temporary-purchased-beat-id") Long id);
	
	@PostMapping("/purchased-beat-after-payment")
	public PurchasedBeatResponse purchaseBeatAfterPayment(@RequestBody PurchasedBeatRequest request);
	
	@DeleteMapping("/temporary-purchased-beat/{temporary-purchased-beat-id}")
	public Void deleteTemporaryPurchasedBeatById(@PathVariable("temporary-purchased-beat-id") Long id);
	
	@GetMapping("/find-purchased-beat-by-beat-id/{beat-id}")
	public Boolean findPurchasedBeatByBeatId(@PathVariable("beat-id") Long beatId);
}
