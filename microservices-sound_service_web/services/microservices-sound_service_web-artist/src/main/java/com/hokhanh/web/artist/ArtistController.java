package com.hokhanh.web.artist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/artists")
@RequiredArgsConstructor
@RestController
public class ArtistController {
	
	private final ArtistService service;
	
	@PostMapping
	public ResponseEntity<ArtistResponse> createArtist(@RequestPart @Valid ArtistRequest request
			,@RequestPart("imageFile") MultipartFile imageFile, @RequestPart("trackFile") MultipartFile trackFile, @RequestPart("token") String token) throws URISyntaxException, IOException, InterruptedException{
		return ResponseEntity.ok(service.createArtist(request, imageFile, trackFile, token));
	}
	
	@PutMapping
	public ResponseEntity<ArtistResponse> updateArtist(@RequestPart @Valid ArtistRequest request
			,@RequestPart("imageFile") MultipartFile imageFile, @RequestPart("trackFile") MultipartFile trackFile){
		return ResponseEntity.ok(service.updateArtist(request, imageFile, trackFile));
	}
	
	@GetMapping("/{artist-id}")
	public ResponseEntity<ArtistResponse> findById(@PathVariable("artist-id") String artistId){
		return ResponseEntity.ok(service.findById(artistId));
	}
	
	@GetMapping("/sound-makers/find-all")
	public ResponseEntity<ListArtistResponse> findAllSoundMakers(@RequestParam int page) {
		return ResponseEntity.ok(new ListArtistResponse(service.findAllSoundMakers(page), service.countAllSoundMakers()));
	}
	
	@GetMapping("/sound-checkers/find-all")
	public ResponseEntity<ListArtistResponse> findAllSoundChecker(@RequestParam int page) {
		return ResponseEntity.ok(new ListArtistResponse(service.findAllSoundChecker(page), service.countAllSoundCheckers()));
	}
	
	@GetMapping("/sound-makers/find-by-skill-or-artist-name")
	public ResponseEntity<ListArtistResponse> findBySkillOrArtistNameSoundMaker(@RequestParam("artist-name") String artistName, 
			@RequestParam("job-type-id") Long jobTypeIdSearch, @RequestParam int page) {
		return ResponseEntity.ok(new ListArtistResponse(service.findBySkillOrArtistNameSoundMakers(artistName, jobTypeIdSearch, page), 
				service.countBySkillOrArtistNameSoundMakers(artistName, jobTypeIdSearch)));
	}
	
	@GetMapping("/sound-checkers/find-by-artist-name")
	public ResponseEntity<ListArtistResponse> findByArtistNameSoundCheckers(@RequestParam("artist-name") String artistName,
			@RequestParam int page) {
		return ResponseEntity.ok(new ListArtistResponse(service.findByArtistNameSoundCheckers(artistName, page), 
				service.countByArtistNameSoundCheckers(artistName)));
	}
	
//	lấy hình ảnh trong folder ra
	@GetMapping("/images/{file}")
	public ResponseEntity<Resource> getImageFromFolder(@PathVariable("file") String file) throws MalformedURLException{
		Path path = Paths.get("src/main/resources/static/images/"+file);
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
	}
	
	
//	lấy track trong folder ra
	@GetMapping("/tracks/{file}")
	public ResponseEntity<Resource> getTrackFromFolder(@PathVariable("file") String file) throws MalformedURLException{
		Path path = Paths.get("src/main/resources/static/tracks/"+file);
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
	}
	
//	sau khi đăng nhập thì lấy thông tin artist theo userId
	@GetMapping("/find-by-user-id/{user-id}")
	public ResponseEntity<ArtistResponse> findByUserId(@PathVariable("user-id") String userId) {
		return ResponseEntity.ok(service.findByUserId(userId));
	}
}
