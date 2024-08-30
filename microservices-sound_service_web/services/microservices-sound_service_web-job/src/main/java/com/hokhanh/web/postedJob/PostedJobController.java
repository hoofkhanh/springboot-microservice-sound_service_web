package com.hokhanh.web.postedJob;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@RestController
public class PostedJobController {

	private final PostedJobService service;
	
	@PostMapping("/posted-job/upload-track")
	public ResponseEntity<String> uploadTrack(@RequestParam("trackFile") MultipartFile trackFile){
		return ResponseEntity.ok(service.uploadTrack(trackFile));
	}
	
	@PostMapping("/posted-job")
	public ResponseEntity<PostedJobResponse> postJob(@RequestBody @Valid PostedJobRequest request){
		return ResponseEntity.ok(service.postJob(request));
	}
	
	@PutMapping("/posted-job")
	public ResponseEntity<PostedJobResponse> updatePostedJob(@RequestBody @Valid PostedJobRequest request){
		return ResponseEntity.ok(service.updatePostedJob(request));
	}
	
	@GetMapping("/posted-job/{posted-job-id}")
	public ResponseEntity<PostedJobResponse> findById(@PathVariable("posted-job-id") Long postedJobId){
		return ResponseEntity.ok(service.findById(postedJobId));
	}
	
	@DeleteMapping("/posted-job/{posted-job-id}")
	public ResponseEntity<Void> deleteById(@PathVariable("posted-job-id") Long postedJobId){
		service.deleteById(postedJobId);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/posted-job")
	public ResponseEntity<ListPostedJobResponse> findAll(@RequestParam int page){
		return ResponseEntity.ok(new ListPostedJobResponse(service.findAll(page), service.countAll()));
	}
	
	@GetMapping("/posted-job/find-by-job-type-id/{job-type-id}")
	public ResponseEntity<ListPostedJobResponse> findByJobTypeId(@PathVariable("job-type-id") Long jobTypeId, @RequestParam int page){
		if(jobTypeId == 0) {
			return ResponseEntity.ok(new ListPostedJobResponse(service.findAll(page), service.countAll()));
		}
		
		return ResponseEntity.ok(new ListPostedJobResponse(service.findByJobTypeId(jobTypeId, page), service.countByJobTypeId(jobTypeId)));
	}
	
	@GetMapping("/posted-job/find-by-poster-id/{poster-id}")
	public ResponseEntity<List<PostedJobResponse>> findByPosterId(@PathVariable("poster-id") String posterId){
		return ResponseEntity.ok(service.findByPosterId(posterId));
	}
	
	@GetMapping("/posted-job/tracks/{file}")
	public ResponseEntity<Resource> getTrackFromFolder(@PathVariable("file") String file) throws MalformedURLException{
		Path path = Paths.get("src/main/resources/static/tracks/"+file);
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
	}
}
