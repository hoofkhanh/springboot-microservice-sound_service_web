package com.hokhanh.web.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileToFolder {

	private final String UPLOAD_FILE_OF_BEAT_TO_FOLDER = "D:\\SpringToolSuite-Project\\SpringBoot\\microservices-sound_service_web\\"
			+ "services\\microservices-sound_service_web-beat\\src\\main\\resources\\static\\beats";
	
	public String uploadFileOfBeat(MultipartFile beat) {
		try {
			Files.copy(beat.getInputStream(), Paths.get(UPLOAD_FILE_OF_BEAT_TO_FOLDER + File.separator, beat.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beat.getOriginalFilename();
	}
}
