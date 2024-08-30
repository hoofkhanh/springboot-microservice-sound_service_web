package com.hokhanh.web.hire.soundMaker;

import org.springframework.stereotype.Service;

import com.hokhanh.web.common.Hirer_Expert_Response;
import com.hokhanh.web.jobType.JobTypeResponse;


@Service
public class SoundMakerHireMapper {

	public SoundMakerHire toSoundMakerHire( SoundMakerHireRequest request, Hirer_Expert_Response expertResponse) {
		return SoundMakerHire.builder()
				.hirerId(request.hirerId())
				.expertId(request.expertId())
				.jobTypeId(request.jobTypeId())
				.startDate(request.startDate())
				.endDate(request.endDate())
				.price(expertResponse.hireCost())
				.build();
	}

	public SoundMakerHireResponse toSoundMakerHireResponse(SoundMakerHire soundMakerHire, Hirer_Expert_Response hireResponse,
			Hirer_Expert_Response expertResponse, JobTypeResponse jobTypeResponse) {
		return new SoundMakerHireResponse(
				soundMakerHire.getId(), 
				hireResponse, 
				expertResponse, 
				jobTypeResponse,
				soundMakerHire.getStartDate(),
				soundMakerHire.getEndDate(), 
				soundMakerHire.getPrice()
				);
	}

}
