package com.hokhanh.web.hire;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.artist.ArtistType;
import com.hokhanh.web.artist.SkillResponse;
import com.hokhanh.web.common.Hirer_Expert_Response;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.HireException;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHire;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireMapper;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireRequest;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireResponse;
import com.hokhanh.web.hire.soundChecker.SoundCheckerRepository;
import com.hokhanh.web.hire.soundMaker.SoundMakerHire;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireMapper;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRepository;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRequest;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireResponse;
import com.hokhanh.web.jobType.JobTypeClient;
import com.hokhanh.web.jobType.JobTypeResponse;
import com.hokhanh.web.kafka.HireConfirmation;
import com.hokhanh.web.kafka.HireProducer;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HireService {

	private final SoundMakerHireRepository soundMakerRepo;
	private final SoundMakerHireMapper soundMakerMapper;
	
	private final SoundCheckerRepository soundCheckerRepo;
	private final SoundCheckerHireMapper soundCheckerMapper  ;
	
	private final HireProducer producer;
	
	private final CustomerClient customerClient;
	private final ArtistClient artistClient;
	private final JobTypeClient jobTypeClient;
	
	public void confirmSoundMakerHireInMailOfSoundMaker( SoundMakerHireRequest request) {
		
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		boolean checkSkill = false;
		for (SkillResponse skill : expertResponse.skills()) {
			if(skill.jobTypeId() == request.jobTypeId()) {
				checkSkill = true;
			}
		}
		
		if(checkSkill == false) {
			throw new HireException("Expert (artist) don't have this skill Id: "+ request.jobTypeId());
		}
		
		var jobTypeResponse = jobTypeClient.findById(request.jobTypeId());
		
		producer.sendHireConfirmationToMailOfSoundMakerChecker(
					new HireConfirmation(
							hireResponse, 
							expertResponse, 
							jobTypeResponse, 
							request.startDate(), 
							request.endDate(),
							expertResponse.hireCost()
						)
				);
	}

	public void confirmSoundMakerHireInMailOfHirer( SoundMakerHireRequest request) {
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		JobTypeResponse jobTypeResponse = null;
		
		if(request.jobTypeId() != null && request.jobTypeId() !=0) {
			boolean checkSkill = false;
			for (SkillResponse skill : expertResponse.skills()) {
				if(skill.jobTypeId() == request.jobTypeId()) {
					checkSkill = true;
				}
			}
			
			if(checkSkill == false) {
				throw new HireException("Expert (artist) don't have this skill Id: "+ request.jobTypeId());
			}
			
			jobTypeResponse = jobTypeClient.findById(request.jobTypeId());
		}
		
		producer.sendHireConfirmationToMailOfHirer(
					new HireConfirmation(
							hireResponse, 
							expertResponse, 
							jobTypeResponse, 
							request.startDate(), 
							request.endDate(),
							expertResponse.hireCost()
						)
				);
	}
	
	@Transactional
	public SoundMakerHireResponse saveSoundMakerHire( SoundMakerHireRequest request) {
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false ) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		var jobTypeResponse = jobTypeClient.findById(request.jobTypeId());
		
		if(soundMakerRepo.findByStartDateAndEndDateAndHirerIdAndExpertIdAndJobTypeId(request.startDate(), request.endDate(), request.hirerId()
				, request.expertId(), request.jobTypeId()) != null) {
			throw new  HireException("Can't hire more: ");
		}
		
		var soundMakerHire = soundMakerRepo.save(soundMakerMapper.toSoundMakerHire(request, expertResponse));
		
		return soundMakerMapper.toSoundMakerHireResponse(soundMakerHire, hireResponse, expertResponse, jobTypeResponse);
	}
	
	public void confirmSoundCheckerHireInMailOfSoundChecker( SoundCheckerHireRequest request) {
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		producer.sendHireConfirmationToMailOfSoundMakerChecker(
					new HireConfirmation(
							hireResponse, 
							expertResponse, 
							null, 
							request.startDate(), 
							request.endDate(),
							expertResponse.hireCost()
						)
				);
	}

	public void confirmSoundCheckerHireInMailOfHirer( SoundCheckerHireRequest request) {
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		producer.sendHireConfirmationToMailOfHirer(
					new HireConfirmation(
							hireResponse, 
							expertResponse, 
							null, 
							request.startDate(), 
							request.endDate(),
							expertResponse.hireCost()
						)
				);
	}

	@Transactional
	public SoundCheckerHireResponse saveSoundCheckerHire( SoundCheckerHireRequest request) {
		Hirer_Expert_Response hireResponse = null;
		try {
			hireResponse = customerClient.findById(request.hirerId());
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ request.hirerId());
		}
		
		Hirer_Expert_Response expertResponse = null;
		try {
			expertResponse = artistClient.findById(request.expertId());
			if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false ) {
				throw new HireException("Artist Type is not suitable");
			}
		} catch (FeignClientException e) {
			throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ request.expertId());
		}
		
		if(soundCheckerRepo.findByStartDateAndEndDateAndHirerIdAndExpertId(request.startDate(), request.endDate(), request.hirerId()
				, request.expertId() ) != null) {
			throw new  HireException("Can't hire more: ");
		}
		
		
		var soundChecker = soundCheckerRepo.save(soundCheckerMapper.toSoundCheckerHire(request, expertResponse));
		
		return soundCheckerMapper.toSoundMakerHireResponse(soundChecker, hireResponse, expertResponse);
	}

	public List<SoundMakerHireResponse> taskOfValidDateSoundMaker(String soundMakerId) {
		return soundMakerRepo.findByExpertIdAndEndDateGreaterThanEqual(soundMakerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundMakerHire::getEndDate))
				.map(soundMakerHire -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundMakerHire.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundMakerHire.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundMakerHire.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundMakerHire.getExpertId());
					}
					
					var jobTypeResponse = jobTypeClient.findById(soundMakerHire.getJobTypeId());
					
					return soundMakerMapper.toSoundMakerHireResponse(soundMakerHire, hireResponse, expertResponse, jobTypeResponse);
				})
				.toList();
	}
	
	public List<SoundMakerHireResponse> taskOfValidDateSoundMakerOfHirer(String hirerId) {
		return soundMakerRepo.findByHirerIdAndEndDateGreaterThanEqual(hirerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundMakerHire::getEndDate))
				.map(soundMakerHire -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundMakerHire.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundMakerHire.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundMakerHire.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundMakerHire.getExpertId());
					}
					
					var jobTypeResponse = jobTypeClient.findById(soundMakerHire.getJobTypeId());
					
					return soundMakerMapper.toSoundMakerHireResponse(soundMakerHire, hireResponse, expertResponse, jobTypeResponse);
				})
				.toList();
	}
	
	public List<SoundMakerHireResponse> taskOfInvalidDateSoundMaker(String soundMakerId) {
		return soundMakerRepo.findByExpertIdAndEndDateLessThan(soundMakerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundMakerHire::getEndDate).reversed())
				.map(soundMakerHire -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundMakerHire.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundMakerHire.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundMakerHire.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundMakerHire.getExpertId());
					}
					
					var jobTypeResponse = jobTypeClient.findById(soundMakerHire.getJobTypeId());
					
					return soundMakerMapper.toSoundMakerHireResponse(soundMakerHire, hireResponse, expertResponse, jobTypeResponse);
				})
				.toList();
	}
	
	public List<SoundMakerHireResponse> taskOfInvalidDateSoundMakerOfHirer(String hirerId) {
		return soundMakerRepo.findByHirerIdAndEndDateLessThan(hirerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundMakerHire::getEndDate).reversed())
				.map(soundMakerHire -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundMakerHire.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundMakerHire.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundMakerHire.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_MAKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundMakerHire.getExpertId());
					}
					
					var jobTypeResponse = jobTypeClient.findById(soundMakerHire.getJobTypeId());
					
					return soundMakerMapper.toSoundMakerHireResponse(soundMakerHire, hireResponse, expertResponse, jobTypeResponse);
				})
				.toList();
	}
	
	

	public List<SoundCheckerHireResponse> taskOfValidDateSoundChecker(String soundCheckerId) {
		return soundCheckerRepo.findByExpertIdAndEndDateGreaterThanEqual(soundCheckerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundCheckerHire::getEndDate))
				.map(soundChecker -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundChecker.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundChecker.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundChecker.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundChecker.getExpertId());
					}
					
					return soundCheckerMapper.toSoundMakerHireResponse(soundChecker, hireResponse, expertResponse);
				})
				.toList();
	}
	
	public List<SoundCheckerHireResponse> taskOfValidDateSoundCheckerOfHirer(String hirerId) {
		return soundCheckerRepo.findByHirerIdAndEndDateGreaterThanEqual(hirerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundCheckerHire::getEndDate))
				.map(soundChecker -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundChecker.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundChecker.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundChecker.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundChecker.getExpertId());
					}
					
					return soundCheckerMapper.toSoundMakerHireResponse(soundChecker, hireResponse, expertResponse);
				})
				.toList();
	}


	public List<SoundCheckerHireResponse> taskOfInvalidDateSoundChecker(String soundCheckerId) {
		return soundCheckerRepo.findByExpertIdAndEndDateLessThan(soundCheckerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundCheckerHire::getEndDate).reversed())
				.map(soundChecker -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundChecker.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundChecker.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundChecker.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundChecker.getExpertId());
					}
					
					return soundCheckerMapper.toSoundMakerHireResponse(soundChecker, hireResponse, expertResponse);
				})
				.toList();
	}

	public List<SoundCheckerHireResponse> taskOfInvalidDateSoundCheckerOfHirer(String hirerId) {
		return soundCheckerRepo.findByHirerIdAndEndDateLessThan(hirerId, LocalDate.now())
				.stream()
				.sorted(Comparator.comparing(SoundCheckerHire::getEndDate).reversed())
				.map(soundChecker -> {
					Hirer_Expert_Response hireResponse = null;
					try {
						hireResponse = customerClient.findById(soundChecker.getHirerId());
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (hirer) customer with this id: "+ soundChecker.getHirerId());
					}
					
					Hirer_Expert_Response expertResponse = null;
					try {
						expertResponse = artistClient.findById(soundChecker.getExpertId());
						if(expertResponse.artistType().equals(ArtistType.SOUND_CHECKER) == false ) {
							throw new HireException("Artist Type is not suitable");
						}
					} catch (FeignClientException e) {
						throw new HireException("Not found SoundMakerHire (expert) artist with this id: "+ soundChecker.getExpertId());
					}
					
					return soundCheckerMapper.toSoundMakerHireResponse(soundChecker, hireResponse, expertResponse);
				})
				.toList();
	}


}
