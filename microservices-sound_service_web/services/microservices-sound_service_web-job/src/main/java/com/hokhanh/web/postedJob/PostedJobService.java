package com.hokhanh.web.postedJob;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.web.common.PosterResponse;
import com.hokhanh.web.common.UploadFileToFolder;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.PostedJobException;
import com.hokhanh.web.jobType.JobType;
import com.hokhanh.web.jobType.JobTypeService;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostedJobService {

	private final PostedJobRepository repo;
	private final PostedJobMapper mapper;
	private final CustomerClient customerClient;
	
	private final JobTypeService jobTypeService;
	
	private final UploadFileToFolder fileToFolder;
	
	@Transactional
	public PostedJobResponse postJob( PostedJobRequest request) {
		var jobTypeResponse = jobTypeService.findById(request.jobTypeId());
		
		PosterResponse posterResponse = null;
		try {
			posterResponse = customerClient.findById(request.posterId());
		} catch (FeignClientException e) {
			throw new PostedJobException("Not found Job (poster) customer with this id: "+ request.posterId());
		}
		
		var postedJob = repo.save(mapper.toPostedJob(request));
		
		return mapper.toPostedJobResponse(postedJob, jobTypeResponse, posterResponse);
	}
	
	@Transactional
	public PostedJobResponse updatePostedJob( PostedJobRequest request) {
		var postedJob = repo.findById(request.id())
				.orElseThrow(() -> new PostedJobException("Not found PostedJob with id: "+ request.id()));
		
		var jobTypeResponse = jobTypeService.findById(request.jobTypeId());
		
		PosterResponse posterResponse = null;
		try {
			posterResponse = customerClient.findById(postedJob.getPosterId());
		} catch (FeignClientException e) {
			throw new PostedJobException("Not found Job (poster) customer with this id: "+ request.posterId());
		}
		
		postedJob.setJobType(JobType.builder().id(jobTypeResponse.id()).build());
		postedJob.setTopic(request.topic());
		postedJob.setContent(request.content());
		postedJob.setTrackFile(request.trackFile());
		
		postedJob = repo.save(postedJob);
		
		return mapper.toPostedJobResponse(postedJob, jobTypeResponse, posterResponse);
	}

	public PostedJobResponse findById(Long postedJobId) {
		var postedJob = repo.findById(postedJobId)
				.orElseThrow(() -> new PostedJobException("Not found PostedJob with id: "+ postedJobId));
				
		var jobTypeResponse = jobTypeService.findById(postedJob.getJobType().getId());
		
		PosterResponse posterResponse = null;
		try {
			posterResponse = customerClient.findById(postedJob.getPosterId());
		} catch (FeignClientException e) {
			throw new PostedJobException("Not found Job (poster) customer with this id: "+ postedJob.getPosterId());
		}
		
		return mapper.toPostedJobResponse(postedJob, jobTypeResponse, posterResponse);
	}

	public void deleteById(Long postedJobId) {
		repo.findById(postedJobId)
				.orElseThrow(() -> new PostedJobException("Not found PostedJob with id: "+ postedJobId));
		
		repo.deleteById(postedJobId);;
	}

	public String uploadTrack(MultipartFile trackFile) {
		if(!trackFile.isEmpty()) {
			return fileToFolder.uploadFileOfTrack(trackFile);
		}else {
			return "No upload track";
		}
		
	}

	public List<PostedJobResponse> findAll(int page) {
		return repo.findAll(PageRequest.of(page, 3, Sort.by("id").descending()))
				.stream()
				.sorted(Comparator.comparing(PostedJob::getPostDate).reversed())
				.map(postedJob -> {
					var jobTypeResponse = jobTypeService.findById(postedJob.getJobType().getId());
					
					PosterResponse posterResponse = null;
					try {
						posterResponse = customerClient.findById(postedJob.getPosterId());
					} catch (FeignClientException e) {
						throw new PostedJobException("Not found Job (poster) customer with this id: "+ postedJob.getPosterId());
					}
					
					return mapper.toPostedJobResponse(postedJob, jobTypeResponse, posterResponse);
				})
				.collect(Collectors.toList());
	}
	
	public long countAll() {
		return repo.count();
	}

	public List<PostedJobResponse> findByPosterId(String posterId) {
		try {
			customerClient.findById(posterId);
		} catch (FeignClientException e) {
			throw new PostedJobException("Not found Job (poster) customer with this id: "+ posterId);
		}
		
		var postedJob = repo.findByPosterIdOrderByIdDesc(posterId);
		var postedJobResponse = new ArrayList<PostedJobResponse>();
		
		for (PostedJob job : postedJob) {
			var jobTypeResponse = jobTypeService.findById(job.getJobType().getId());
			
			PosterResponse posterResponse = null;
			try {
				posterResponse = customerClient.findById(job.getPosterId());
			} catch (FeignClientException e) {
				throw new PostedJobException("Not found Job (poster) customer with this id: "+ job.getPosterId());
			}
			
			postedJobResponse.add(mapper.toPostedJobResponse(job, jobTypeResponse, posterResponse));
		}
		
		return postedJobResponse;
	}

	public List<PostedJobResponse> findByJobTypeId(Long jobTypeId, int page) {
		var jobTypeResponse = jobTypeService.findById(jobTypeId);
		
		var postedJob = repo.findByJobType(
				JobType.builder()
				.id(jobTypeResponse.id())
				.name(jobTypeResponse.name())
				.build(),
				PageRequest.of(page, 3, Sort.by("id").descending())
				);
		
		var postedJobResponse = new ArrayList<PostedJobResponse>();
		
		for (PostedJob job : postedJob) {
			
			PosterResponse posterResponse = null;
			try {
				posterResponse = customerClient.findById(job.getPosterId());
			} catch (FeignClientException e) {
				throw new PostedJobException("Not found Job (poster) customer with this id: "+ job.getPosterId());
			}
			
			postedJobResponse.add(mapper.toPostedJobResponse(job, jobTypeResponse, posterResponse));
		}
		
		return postedJobResponse;
	}
	
	public long countByJobTypeId(Long jobTypeId) {
		return repo.countByJobType_Id(jobTypeId);
	}

	
}
