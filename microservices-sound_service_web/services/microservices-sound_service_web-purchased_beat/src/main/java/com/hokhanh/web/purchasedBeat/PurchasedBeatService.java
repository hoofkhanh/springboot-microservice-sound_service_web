package com.hokhanh.web.purchasedBeat;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.beat.BeatClient;
import com.hokhanh.web.common.Seller_Purchaser_Response;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.PurchasedBeatExpcetion;
import com.hokhanh.web.kafka.TemporaryPurchasedBeatConfirmation;
import com.hokhanh.web.kafka.TemporaryPurchasedBeatProducer;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchasedBeatService {

	private final PurchasedBeatRepository repo;
	private final TemporaryPurchasedBeatRepository tempRepo;
	
	private final PurchasedBeatMapper mapper;
	private final TemporaryPurchasedBeatMapper tempMapper;
	
	private final BeatClient beatClient;
	
	private final CustomerClient customerClient;
	private final ArtistClient artistClient;
	
	private final TemporaryPurchasedBeatProducer producer;
	
	@Transactional
	public TemporaryPurchasedBeatResponse purchaseBeatBeforePayment( TemporaryPurchasedBeatRequest request) {
		var postedBeatResponse = beatClient.findByBeatId(request.beatId());
		
		Seller_Purchaser_Response sellerResponse = null;
		try {
			sellerResponse = artistClient.findById(request.sellerId());
		} catch (FeignClientException e) {
			throw new PurchasedBeatExpcetion("Not found TempPurchasedBeat (seller) artist with this id: "+ request.sellerId());
		}
		
		Seller_Purchaser_Response purchaserResponse = null;
		try {
			purchaserResponse = customerClient.findById(request.purchaserId());
		} catch (FeignClientException e) {
			throw new PurchasedBeatExpcetion("Not found TempPurchasedBeat (purchaser) customer with this id: "+ request.purchaserId());
		}
		
		var tempPurchasedBeat = tempRepo.save(tempMapper.toTempPurchasedBeat(request, postedBeatResponse.price()));
		
		var tempPurchasedBeatConfirmation = new TemporaryPurchasedBeatConfirmation(
					tempPurchasedBeat.getId(),
					postedBeatResponse.beat(),
					sellerResponse,
					purchaserResponse,
					tempPurchasedBeat.getPurchaseDate(),
					tempPurchasedBeat.getPurchasedPrice()
				);
		producer.sendTemporaryPurchasedBeatConfirmation(tempPurchasedBeatConfirmation);
		
		return tempMapper.toTempPurchasedBeatResponse(tempPurchasedBeat, postedBeatResponse.beat(), sellerResponse, purchaserResponse);
	}

	@Transactional
	public PurchasedBeatResponse purchaseBeatAfterPayment( PurchasedBeatRequest request) {
		var postedBeatResponse = beatClient.findByBeatId(request.beatId());
		
		Seller_Purchaser_Response sellerResponse = null;
		try {
			sellerResponse = artistClient.findById(request.sellerId());
		} catch (FeignClientException e) {
			throw new PurchasedBeatExpcetion("Not found PurchasedBeat (seller) artist with this id: "+ request.sellerId());
		}
		
		Seller_Purchaser_Response purchaserResponse = null;
		try {
			purchaserResponse = customerClient.findById(request.purchaserId());
		} catch (FeignClientException e) {
			throw new PurchasedBeatExpcetion("Not found PurchasedBeat (purchaser) customer with this id: "+ request.purchaserId());
		}
		
		beatClient.deleteByBuyingBeat(postedBeatResponse.id());
		
		var purchasedBeat = repo.save(mapper.toPurchasedBeat(request, postedBeatResponse.price()));
		
		return mapper.toPurchasedBeatResponse(purchasedBeat, postedBeatResponse.beat(), sellerResponse, purchaserResponse);
	}

	public TemporaryPurchasedBeat findTemporaryPurchasedBeatById(Long id) {
		return tempRepo.findById(id)
				.orElseThrow(() -> new PurchasedBeatExpcetion("Not found TempPurchasedBeat with this id: "+ id));
	}

	public String deleteTemporaryPurchasedBeatById(Long id) {
		tempRepo.findById(id)
				.orElseThrow(() -> new PurchasedBeatExpcetion("Not found TempPurchasedBeat with this id: "+ id));
		
		tempRepo.deleteById(id);
		return "Hủy thành công";
	}

	public Boolean findPurchasedBeatByBeatId(Long beatId) {
		var purchasedBeat = repo.findByBeatId(beatId);
		if(purchasedBeat == null) {
			return false;
		}else {
			return true;
		}
	}

	public List<PurchasedBeatResponse> getPurchasedBeatOfPurchaserByPurchaserId(String purchaserId) {
		return repo.findByPurchaserId(purchaserId)
				.stream()
				.map(purchasedBeat -> {
					var beat = beatClient.findById(purchasedBeat.getBeatId());
					
					Seller_Purchaser_Response sellerResponse = null;
					try {
						sellerResponse = artistClient.findById(purchasedBeat.getSellerId());
					} catch (FeignClientException e) {
						throw new PurchasedBeatExpcetion("Not found PurchasedBeat (seller) artist with this id: "+ purchasedBeat.getSellerId());
					}
					
					Seller_Purchaser_Response purchaserResponse = null;
					try {
						purchaserResponse = customerClient.findById(purchasedBeat.getPurchaserId());
					} catch (FeignClientException e) {
						throw new PurchasedBeatExpcetion("Not found PurchasedBeat (purchaser) customer with this id: "+ purchasedBeat.getPurchaserId());
					}
															
					return mapper.toPurchasedBeatResponse(purchasedBeat, beat, sellerResponse, purchaserResponse);
				})
				.toList();
	}
}
