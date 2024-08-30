package com.hokhanh.web.beat;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/beats")
@RequiredArgsConstructor
@RestController
public class BeatController {
	
	private final BeatService service;

	@PostMapping("/beat/upload-beat-file")
	public ResponseEntity<String> uploadBeatFile(@RequestParam("beatFile") MultipartFile beatFile){
		return ResponseEntity.ok(service.uploadBeatFile(beatFile));
	}
	
	@GetMapping("/beat/{beat-id}")
	public ResponseEntity<BeatResponse> findById(@PathVariable("beat-id") Long id){
		return ResponseEntity.ok(service.findById(id));
	} 
	
	@GetMapping("/beat/get-beat/{beat-file}")
	public ResponseEntity<Resource> getBeatFromStatic(@PathVariable("beat-file") String beatFile) throws MalformedURLException{
		Path path = Paths.get("src/main/resources/static/beats/"+beatFile);
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
	} 
}
