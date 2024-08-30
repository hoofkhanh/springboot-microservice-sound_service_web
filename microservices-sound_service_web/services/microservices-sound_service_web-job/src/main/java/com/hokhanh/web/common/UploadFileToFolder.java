package com.hokhanh.web.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileToFolder {

	private final String UPLOAD_FILE_OF_TRACK_TO_FOLDER = "D:\\SpringToolSuite-Project\\SpringBoot\\"
			+ "microservices-sound_service_web\\services\\microservices-sound_service_web-job\\src\\main\\resources\\static\\tracks";
	
	public String uploadFileOfTrack(MultipartFile track) {
		try {
			Files.copy(track.getInputStream(), Paths.get(UPLOAD_FILE_OF_TRACK_TO_FOLDER + File.separator, track.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return track.getOriginalFilename();
	}
}
