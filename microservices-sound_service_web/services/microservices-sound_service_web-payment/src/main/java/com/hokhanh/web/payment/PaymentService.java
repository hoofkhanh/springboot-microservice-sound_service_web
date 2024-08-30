package com.hokhanh.web.payment;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.common.Seller_Purchaser_Response;
import com.hokhanh.web.hire.HireClient;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireRequest;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRequest;
import com.hokhanh.web.kafka.PaymentCategory;
import com.hokhanh.web.kafka.SuccessfulPaymentNotificationProducer;
import com.hokhanh.web.kafka.SuccessfulPaymentNotification;
import com.hokhanh.web.purchasedBeat.PurchasedBeatClient;
import com.hokhanh.web.purchasedBeat.PurchasedBeatRequest;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository repo;
	
	private final PurchasedBeatClient purchasedBeatClient;
	
	private final SuccessfulPaymentNotificationProducer successfulPaymentNotificationProducer;
	
	private final HireClient hireClient;

	@Transactional
	public String beatPayment(Long tempPurchasedBeatId, PaymentMethod paymentMethod) {
		try {
			var tempPurchasedBeat = purchasedBeatClient.findTemporaryPurchasedBeatById(tempPurchasedBeatId);
			
			var purchasedBeatCheck = purchasedBeatClient.findPurchasedBeatByBeatId(tempPurchasedBeat.getBeatId());
			if(purchasedBeatCheck == true) {
				purchasedBeatClient.deleteTemporaryPurchasedBeatById(tempPurchasedBeatId);
				return "Other person is purchased";
			}
			
			var purchasedBeatResponse =  purchasedBeatClient.purchaseBeatAfterPayment(
						new PurchasedBeatRequest(tempPurchasedBeat.getBeatId(), 
								tempPurchasedBeat.getSellerId(), 
								tempPurchasedBeat.getPurchaserId(),
								tempPurchasedBeat.getPurchasedPrice())
					);
			
			Payment payment = Payment.builder()
					.purchasedBeatId(purchasedBeatResponse.id())
					.amount(purchasedBeatResponse.purchasedPrice())
					.date(LocalDate.now())
					.paymentMethod(paymentMethod)
					.build();
			
			purchasedBeatClient.deleteTemporaryPurchasedBeatById(tempPurchasedBeatId);
			
			payment = repo.save(payment);
			
			successfulPaymentNotificationProducer.sendSuccessfullPaymentNotification(
						new SuccessfulPaymentNotification(
								payment.getId(), 
								payment.getAmount(), 
								paymentMethod, 
								PaymentCategory.PURCHASE_BEAT,
								purchasedBeatResponse.seller(),
								purchasedBeatResponse.purchaser(),
								payment.getDate()
								)
					);
			
			return "Successfull";
		} catch (FeignClientException e) {
			e.printStackTrace();
			return "Beat is purchased formerly";
		}
	}
	
	@Transactional
	public String soundMakerHirePayment(SoundMakerHireRequest request, PaymentMethod paymentMethod) {
		try {
			var soundMakerHire = hireClient.saveSoundMakerHire(request);
			
			Payment payment = Payment.builder()
					.soundMakerHireId(soundMakerHire.id())
					.amount(soundMakerHire.price())
					.date(LocalDate.now())
					.paymentMethod(paymentMethod)
					.build();
			
			payment = repo.save(payment);
			
			successfulPaymentNotificationProducer.sendSuccessfullPaymentNotification(
					new SuccessfulPaymentNotification(
							payment.getId(), 
							payment.getAmount(), 
							paymentMethod, 
							PaymentCategory.HIRE_SOUND_MAKER,
							new Seller_Purchaser_Response(soundMakerHire.expert().id(), soundMakerHire.expert().user(),
									soundMakerHire.expert().artistName()),
							new Seller_Purchaser_Response(soundMakerHire.hirer().id(), soundMakerHire.hirer().user(), 
									soundMakerHire.hirer().artistName()) ,
							payment.getDate()
							)
				);
		
			return "Successfull";
		} catch (Exception e) {
			e.printStackTrace();
			return "Can't hire more";
		}
	}

	@Transactional
	public String soundCheckerHirePayment(SoundCheckerHireRequest request, PaymentMethod paymentMethod) {
		try {
			var soundCheckerHire = hireClient.saveSoundCheckerHire(request);
			
			Payment payment = Payment.builder()
					.soundCheckerHireId(soundCheckerHire.id())
					.amount(soundCheckerHire.price())
					.date(LocalDate.now())
					.paymentMethod(paymentMethod)
					.build();
			
			payment = repo.save(payment);
			
			successfulPaymentNotificationProducer.sendSuccessfullPaymentNotification(
					new SuccessfulPaymentNotification(
							payment.getId(), 
							payment.getAmount(), 
							paymentMethod, 
							PaymentCategory.HIRE_SOUND_CHECKER,
							new Seller_Purchaser_Response(soundCheckerHire.expert().id(), soundCheckerHire.expert().user(),
									soundCheckerHire.expert().artistName()),
							new Seller_Purchaser_Response(soundCheckerHire.hirer().id(), soundCheckerHire.hirer().user(), 
									soundCheckerHire.hirer().artistName()) ,
							payment.getDate()
							)
				);
		
			return "Successfull";
		} catch (Exception e) {
			e.printStackTrace();
			return "Can't hire more";
		}
	}
	
	
}
