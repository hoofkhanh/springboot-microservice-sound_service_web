package com.hokhanh.web.purchasedBeat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/purchased-beats")
@RequiredArgsConstructor
@RestController
public class PurchasedBeatController {

	private final PurchasedBeatService service;
	
	@PostMapping("/purchased-beat-before-payment")
	public ResponseEntity<TemporaryPurchasedBeatResponse> purchaseBeatBeforePayment(@RequestBody @Valid TemporaryPurchasedBeatRequest request){
		return ResponseEntity.ok(service.purchaseBeatBeforePayment(request));
	}
	
	@GetMapping("/purchased-beat-of-purchaser/{purchaser-id}")
	public ResponseEntity<List<PurchasedBeatResponse>> getPurchasedBeatOfPurchaserByPurchaserId(@PathVariable("purchaser-id") String purchaserId){
		return ResponseEntity.ok(service.getPurchasedBeatOfPurchaserByPurchaserId(purchaserId));
	}
	
//	đc call tới
	@PostMapping("/purchased-beat-after-payment")
	public ResponseEntity<PurchasedBeatResponse> purchaseBeatAfterPayment(@RequestBody @Valid PurchasedBeatRequest request){
		return ResponseEntity.ok(service.purchaseBeatAfterPayment(request));
	}
	
//	đc call tới
	@GetMapping("/find-purchased-beat-by-beat-id/{beat-id}")
	public ResponseEntity<Boolean> findPurchasedBeatByBeatId(@PathVariable("beat-id") Long beatId){
		return ResponseEntity.ok(service.findPurchasedBeatByBeatId(beatId));
	} 
	
//	đc call tới
	@GetMapping("/temporary-purchased-beat/{temporary-purchased-beat-id}")
	public ResponseEntity<TemporaryPurchasedBeat> findTemporaryPurchasedBeatById(@PathVariable("temporary-purchased-beat-id") Long id){
		return ResponseEntity.ok(service.findTemporaryPurchasedBeatById(id));
	} 
	
//	đc call tới
	@DeleteMapping("/temporary-purchased-beat/{temporary-purchased-beat-id}")
	public ResponseEntity<Void> deleteTemporaryPurchasedBeatById(@PathVariable("temporary-purchased-beat-id") Long id){
		service.deleteTemporaryPurchasedBeatById(id);
		return ResponseEntity.accepted().build();
	} 
	
//	đc call tới trong html
	@GetMapping("/delete-temporary-purchased-beat-in-mail/{temporary-purchased-beat-id}")
	public ResponseEntity<String> deleteTemporaryPurchasedBeatByIdInMail(@PathVariable("temporary-purchased-beat-id") Long id){
		return ResponseEntity.ok(service.deleteTemporaryPurchasedBeatById(id));
	} 
}
