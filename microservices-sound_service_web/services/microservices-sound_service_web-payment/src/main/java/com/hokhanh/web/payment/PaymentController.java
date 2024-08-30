package com.hokhanh.web.payment;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hokhanh.web.hire.soundChecker.SoundCheckerHireRequest;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRequest;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/payments")
@RequiredArgsConstructor
@RestController
public class PaymentController {

	private final PaymentService service;
	
	
//	api dưới là đc call tới trong html
	@RequestMapping(value =  "/beat-payment", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<String> beatPayment(@RequestParam Long tempPurchasedBeatId,@RequestParam PaymentMethod paymentMethod) {
		return ResponseEntity.ok(service.beatPayment(tempPurchasedBeatId, paymentMethod));
	}
	
	@PostMapping( "/sound-maker-hire-payment")
	public ResponseEntity<String> soundMakerHirePayment(SoundMakerHireRequest request, PaymentMethod paymentMethod) {
		return ResponseEntity.ok(service.soundMakerHirePayment(request, paymentMethod));
	}
	
	@PostMapping( "/sound-checker-hire-payment")
	public ResponseEntity<String> soundCheckerHirePayment(SoundCheckerHireRequest request, PaymentMethod paymentMethod) {
		return ResponseEntity.ok(service.soundCheckerHirePayment(request, paymentMethod));
	}
	
}
