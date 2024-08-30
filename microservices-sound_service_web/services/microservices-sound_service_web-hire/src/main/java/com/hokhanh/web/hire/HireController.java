package com.hokhanh.web.hire;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hokhanh.web.hire.soundChecker.SoundCheckerHireRequest;
import com.hokhanh.web.hire.soundChecker.SoundCheckerHireResponse;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireRequest;
import com.hokhanh.web.hire.soundMaker.SoundMakerHireResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/hires")
@RequiredArgsConstructor
@RestController
public class HireController {
	
	private final HireService service;

	@PostMapping("/confirm-sound-maker-hire-in-mail-of-sound-maker")
	public ResponseEntity<Void> confirmSoundMakerHireInMailOfSoundMaker(@RequestBody @Valid SoundMakerHireRequest request) {
		service.confirmSoundMakerHireInMailOfSoundMaker(request);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/task-of-valid-date-sound-maker/{sound-maker-id}")
	public ResponseEntity<List<SoundMakerHireResponse>> taskOfValidDateSoundMaker(@PathVariable("sound-maker-id") String soundMakerId) {
		return ResponseEntity.ok(service.taskOfValidDateSoundMaker(soundMakerId));
	}
	
	@GetMapping("/task-of-valid-date-sound-maker-of-hirer/{hirer-id}")
	public ResponseEntity<List<SoundMakerHireResponse>> taskOfValidDateSoundMakerOfHirer(@PathVariable("hirer-id") String hirerId) {
		return ResponseEntity.ok(service.taskOfValidDateSoundMakerOfHirer(hirerId));
	}
	
	@GetMapping("/task-of-invalid-date-sound-maker/{sound-maker-id}")
	public ResponseEntity<List<SoundMakerHireResponse>> taskOfInvalidDateSoundMaker(@PathVariable("sound-maker-id") String soundMakerId) {
		return ResponseEntity.ok(service.taskOfInvalidDateSoundMaker(soundMakerId));
	}
	
	@GetMapping("/task-of-invalid-date-sound-maker-of-hirer/{hirer-id}")
	public ResponseEntity<List<SoundMakerHireResponse>> taskOfInvalidDateSoundMakerOfHirer(@PathVariable("hirer-id") String hirerId) {
		return ResponseEntity.ok(service.taskOfInvalidDateSoundMakerOfHirer(hirerId));
	}
	
	@PostMapping("/confirm-sound-checker-hire-in-mail-of-sound-checker")
	public ResponseEntity<Void> confirmSoundCheckerHireInMailOfSoundChecker(@RequestBody @Valid SoundCheckerHireRequest request) {
		service.confirmSoundCheckerHireInMailOfSoundChecker(request);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/task-of-valid-date-sound-checker/{sound-checker-id}")
	public ResponseEntity<List<SoundCheckerHireResponse>> taskOfValidDateSoundChecker(@PathVariable("sound-checker-id") String soundCheckerId) {
		return ResponseEntity.ok(service.taskOfValidDateSoundChecker(soundCheckerId));
	}
	
	@GetMapping("/task-of-valid-date-sound-checker-of-hirer/{hirer-id}")
	public ResponseEntity<List<SoundCheckerHireResponse>> taskOfValidDateSoundCheckerOfHirer(@PathVariable("hirer-id") String hirerId) {
		return ResponseEntity.ok(service.taskOfValidDateSoundCheckerOfHirer(hirerId));
	}
	
	@GetMapping("/task-of-invalid-date-sound-checker/{sound-checker-id}")
	public ResponseEntity<List<SoundCheckerHireResponse>> taskOfInvalidDateSoundChecker(@PathVariable("sound-checker-id") String soundCheckerId) {
		return ResponseEntity.ok(service.taskOfInvalidDateSoundChecker(soundCheckerId));
	}
	
	@GetMapping("/task-of-invalid-date-sound-checker-of-hirer/{hirer-id}")
	public ResponseEntity<List<SoundCheckerHireResponse>> taskOfInvalidDateSoundCheckerOfHirer(@PathVariable("hirer-id") String hirerId) {
		return ResponseEntity.ok(service.taskOfInvalidDateSoundCheckerOfHirer(hirerId));
	}
	
//	api dưới là payment call tới
	@PostMapping("/confirm-sound-maker-hire-in-mail-of-hirer")
	public ResponseEntity<Void> confirmSoundMakerHireInMailOfHirer( @Valid SoundMakerHireRequest request) {
		service.confirmSoundMakerHireInMailOfHirer(request);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/save-sound-maker-hire")
	public ResponseEntity<SoundMakerHireResponse> saveSoundMakerHire(@RequestBody @Valid SoundMakerHireRequest request) {
		return ResponseEntity.ok(service.saveSoundMakerHire(request));
	}
	
	@PostMapping("/confirm-sound-checker-hire-in-mail-of-hirer")
	public ResponseEntity<Void> confirmSoundCheckerHireInMailOfHirer( @Valid SoundCheckerHireRequest request) {
		service.confirmSoundCheckerHireInMailOfHirer(request);
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/save-sound-checker-hire")
	public ResponseEntity<SoundCheckerHireResponse> saveSoundCheckerHire(@RequestBody @Valid SoundCheckerHireRequest request) {
		return ResponseEntity.ok(service.saveSoundCheckerHire(request));
	}
}
