package com.hokhanh.web.beat;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.web.beatGenre.BeatGenreRepository;
import com.hokhanh.web.exception.BeatGenreException;
import com.hokhanh.web.util.UploadFileToFolder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeatService {

	private final UploadFileToFolder toFolder;
	private final BeatRepository repo;
	private final BeatMapper mapper;
	
	private final BeatGenreRepository beatGenreRepo;

	public String uploadBeatFile(MultipartFile beatFile) {
		if (beatFile.isEmpty()) {
			return "No upload beat file";
		} else {
			return toFolder.uploadFileOfBeat(beatFile);
		}
	}

	public BeatResponse createBeat( BeatRequest request) {
		var beatGenre = beatGenreRepo.findById(request.beatGenreId())
				.orElseThrow(() -> new BeatGenreException("Not found beatGenre with this id: "+request.beatGenreId()));
		
		var beat = repo.save(mapper.toBeat(request, beatGenre));
		return mapper.toBeatResponse(beat);
	}

	public BeatResponse findById(Long id) {
		return repo.findById(id)
				.map(beat -> mapper.toBeatResponse(beat))
				.orElseThrow(() -> new BeatGenreException("Not found beat with this id: "+id));
	}
	
	public void deleteById(Long id) {
		repo.findById(id)
			.orElseThrow(() -> new BeatGenreException("Not found beat with this id: "+id));
		repo.deleteById(id); 
	}

}
