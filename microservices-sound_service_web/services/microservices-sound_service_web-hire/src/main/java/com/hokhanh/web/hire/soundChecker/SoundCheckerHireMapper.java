package com.hokhanh.web.hire.soundChecker;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.Hirer_Expert_Response;


@Service
public class SoundCheckerHireMapper {

	public SoundCheckerHire toSoundCheckerHire( SoundCheckerHireRequest request, Hirer_Expert_Response expertResponse) {
		return SoundCheckerHire.builder()
				.hirerId(request.hirerId())
				.expertId(request.expertId())
				.startDate(request.startDate())
				.endDate(request.endDate())
				.price(expertResponse.hireCost())
				.build();
	}

	public SoundCheckerHireResponse toSoundMakerHireResponse(SoundCheckerHire soundChecker, Hirer_Expert_Response hireResponse,
			Hirer_Expert_Response expertResponse) {
		return new SoundCheckerHireResponse(
				soundChecker.getId(), 
				hireResponse, 
				expertResponse, 
				soundChecker.getStartDate(),
				soundChecker.getEndDate(), 
				soundChecker.getPrice()
				);
	}

}
