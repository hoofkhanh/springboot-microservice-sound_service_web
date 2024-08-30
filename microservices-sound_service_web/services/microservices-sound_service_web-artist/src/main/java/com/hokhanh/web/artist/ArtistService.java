package com.hokhanh.web.artist;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bson.json.JsonObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.web.exception.ArtistException;
import com.hokhanh.web.exception.SkillException;
import com.hokhanh.web.jobType.JobTypeClient;
import com.hokhanh.web.jobType.JobTypeResponse;
import com.hokhanh.web.skill.SkillMapper;
import com.hokhanh.web.skill.SkillRepository;
import com.hokhanh.web.user.UserClient;
import com.hokhanh.web.user.UserRequest;
import com.hokhanh.web.user.UserResponse;
import com.hokhanh.web.util.UploadFileToFolder;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
public class ArtistService {

	private final ArtistRepository repo;
	private final ArtistMapper mapper;
	
	private final UserClient userClient;
	
	private final UploadFileToFolder toFolder;
	
	private final JobTypeClient jobTypeClient;
	
	private final SkillRepository skillRepo;
	private final SkillMapper skillMapper;
	
	@Transactional
	public ArtistResponse createArtist( ArtistRequest request, MultipartFile imageFile, MultipartFile trackFile, String token) throws URISyntaxException, IOException, InterruptedException {
		String userIdTemp = null;
		String artistIdTemp = null;
		try {
			if(imageFile.isEmpty()) {
				throw new ArtistException("Need image for artist");
			}
			
			if(trackFile.isEmpty()) {
				throw new ArtistException("Need track for artist");
			}
			
			var userResponse = userClient.createUser(request.user());
			userIdTemp = userResponse.id();
			
			String pathOfImage = null;
			String pathOfTrack = null;
			pathOfImage = toFolder.uploadFileOfImage(imageFile);
			pathOfTrack = toFolder.uploadFileOfTrack(trackFile);
				
			var artist = repo.save(mapper.toArtist(request, userResponse.id(), pathOfImage, pathOfTrack));
			artistIdTemp = artist.getId();
			
			var jobTypeResponseList = new ArrayList<JobTypeResponse>();
			for (Long jobTypeId : request.jobTypeIds()) {
				var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
				jobTypeResponseList.add(jobTypeResponse);
			}
			skillRepo.save(skillMapper.toSkill(artist.getId(), request.jobTypeIds()));
			
			JSONObject credentials = new JSONObject();
	        credentials.put("temporary", false);
	        credentials.put("type", "password");
	        credentials.put("value", userResponse.password());

	        // Tạo JSON Array và thêm "credentials"
	        JSONArray credentialsArray = new JSONArray();
	        credentialsArray.add(credentials);

	        // Tạo JSON cho toàn bộ dữ liệu
	        JSONObject json = new JSONObject();
	        json.put("credentials", credentialsArray);
	        json.put("username", userResponse.email());
	        json.put("firstName", userResponse.fullName().split(" ")[userResponse.fullName().split(" ").length - 1]);
	        json.put("lastName", userResponse.fullName().split(" ")[0]);
	        json.put("email", userResponse.email());
	        json.put("emailVerified", false);
	        json.put("enabled", true);
	        
            
	        HttpClient client = HttpClient.newHttpClient();

	        // Tạo HttpRequest
	        HttpRequest requestUR = HttpRequest.newBuilder()
	                .uri(new URI("http://localhost:9098/admin/realms/microservices-sound_service-web-artist/users"))
	                .header("Content-Type", "application/json")
	                .header("Authorization", "Bearer " + token)
	                .POST(BodyPublishers.ofString(json.toString()))
	                .build();

	        // Gửi yêu cầu và nhận phản hồi
	        HttpResponse<String> response = client.send(requestUR, HttpResponse.BodyHandlers.ofString());
            
            
            // Xử lý phản hồi
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
	            
	            
			
			return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
		} catch (FeignClientException e) {
			if(userIdTemp != null) {
				userClient.deleteUserByID(userIdTemp);
				repo.deleteById(artistIdTemp);
			}
			
			throw new SkillException(e.getMessage());
		}
	}

	@Transactional
	public ArtistResponse updateArtist( ArtistRequest request, MultipartFile imageFile, MultipartFile trackFile) {
		UserResponse userTemp = null;
		try {
			if(request.id() == null) {
				throw new ArtistException("Need the id to update artist");
			}
			
			var artist = repo.findById(request.id())
					.orElseThrow(() -> new ArtistException("Update artist errors because this ID is incorrect : " + request.id()));
			
			if(artist.getUserId().equals(request.user().id()) == false) {
				throw new ArtistException("Can't update artist because userId of artist is incorrect");
			}
			
			userTemp = userClient.findUserById(request.user().id());
			
			var userResponse = userClient.updateUser(request.user());
			
			artist.setArtistName(request.artistName());
			artist.setIntroduction(request.introduction());
			artist.setNameOfIntroductionTrack(request.nameOfIntroductionTrack());
			artist.setArtistType(request.artistType());
			artist.setHireCost(request.hireCost());
			
			if( imageFile != null && !imageFile.isEmpty()) {
				String pathOfImage = toFolder.uploadFileOfImage(imageFile);
				artist.setImage(pathOfImage);
			}
			
			if( trackFile != null && !trackFile.isEmpty()) {
				String pathOfTrack = toFolder.uploadFileOfTrack(trackFile);
				artist.setIntroductionTrackFile(pathOfTrack);
			}
			
			var jobTypeResponseList = new ArrayList<JobTypeResponse>();
			for (Long jobTypeId : request.jobTypeIds()) {
				var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
				jobTypeResponseList.add(jobTypeResponse);
			}
			
			var skill = skillRepo.findByArtistId(artist.getId());
			if(skill == null) {
				throw new SkillException("No Skill with artistId :" + artist.getId());
			}
			skill.setJobTypeIds(request.jobTypeIds());
			skillRepo.save(skill);
			
			return mapper.toArtistResponse(repo.save(artist), userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
		} catch (FeignClientException e) {
			if(userTemp != null) {
				UserRequest userRequest = new UserRequest(
						userTemp.id(), 
						userTemp.email(), 
						userTemp.password(),
						userTemp.fullName(), 
						userTemp.phoneNumber(), 
						userTemp.country(), 
						userTemp.dayOfBirth(), 
						userTemp.role()
					);
				
				userClient.updateUser(userRequest);
			}
			
			throw new SkillException(e.getMessage());
		}
	}

	public ArtistResponse findById(String artistId) {
		var artist =  repo.findById(artistId)
				.orElseThrow(() -> new ArtistException("Artist's findById api error because this ID is incorrect : " + artistId));
		
		var userResponse = userClient.findUserById(artist.getUserId());
		
		var skill = skillRepo.findByArtistId(artist.getId());
		if(skill == null) {
			throw new SkillException("No Skill with artistId :" + artist.getId());
		}
		
		var jobTypeResponseList = new ArrayList<JobTypeResponse>();
		for (Long jobTypeId : skill.getJobTypeIds()) {
			var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
			jobTypeResponseList.add(jobTypeResponse);
		}
		
		return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
	}

	public List<ArtistResponse> findAllSoundMakers(int page) {
		return repo.findByArtistType(ArtistType.SOUND_MAKER, PageRequest.of(page, 4))
				.stream()
				.map(artist -> {
					var userResponse = userClient.findUserById(artist.getUserId());
					
					var skill = skillRepo.findByArtistId(artist.getId());
					if(skill == null) {
						throw new SkillException("No Skill with artistId :" + artist.getId());
					}
					
					var jobTypeResponseList = new ArrayList<JobTypeResponse>();
					for (Long jobTypeId : skill.getJobTypeIds()) {
						var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
						jobTypeResponseList.add(jobTypeResponse);
					}
					
					return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
				})
				.toList();
	}

	public long countAllSoundMakers() {
		return repo.countByArtistType(ArtistType.SOUND_MAKER);
	}
	
	public List<ArtistResponse> findAllSoundChecker(int page) {
		return repo.findByArtistType(ArtistType.SOUND_CHECKER, PageRequest.of(page, 3))
				.stream()
				.map(artist -> {
					var userResponse = userClient.findUserById(artist.getUserId());
					
					var skill = skillRepo.findByArtistId(artist.getId());
					if(skill == null) {
						throw new SkillException("No Skill with artistId :" + artist.getId());
					}
					
					var jobTypeResponseList = new ArrayList<JobTypeResponse>();
					for (Long jobTypeId : skill.getJobTypeIds()) {
						var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
						jobTypeResponseList.add(jobTypeResponse);
					}
					
					return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
				})
				.toList();
	}
	
	public long countAllSoundCheckers() {
		return repo.countByArtistType(ArtistType.SOUND_CHECKER);
	}

	public List<ArtistResponse> findBySkillOrArtistNameSoundMakers(String artistName, Long jobTypeIdSearch, int page) {
		return repo.findByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType.SOUND_MAKER, artistName)
				.stream()
				.map(artist -> {
					var userResponse = userClient.findUserById(artist.getUserId());
					
					var skill = skillRepo.findByArtistId(artist.getId());
					if(skill == null) {
						throw new SkillException("No Skill with artistId :" + artist.getId());
					}
					
					var jobTypeResponseList = new ArrayList<JobTypeResponse>();
					for (Long jobTypeId : skill.getJobTypeIds()) {
						var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
						jobTypeResponseList.add(jobTypeResponse);
					}
					
					return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
				})
				.filter(artist -> artist.skills().stream()
						.anyMatch(skill ->  jobTypeIdSearch==0 || skill.jobTypeId() == jobTypeIdSearch))
				.limit((page +1) *4)
				.toList();
	}
	
	public long countBySkillOrArtistNameSoundMakers(String artistName, Long jobTypeIdSearch) {
		var soundMakers = repo.findByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType.SOUND_MAKER, artistName)
				.stream()
				.map(artist -> {
					var userResponse = userClient.findUserById(artist.getUserId());
					
					var skill = skillRepo.findByArtistId(artist.getId());
					if(skill == null) {
						throw new SkillException("No Skill with artistId :" + artist.getId());
					}
					
					var jobTypeResponseList = new ArrayList<JobTypeResponse>();
					for (Long jobTypeId : skill.getJobTypeIds()) {
						var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
						jobTypeResponseList.add(jobTypeResponse);
					}
					
					return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
				})
				.filter(artist -> artist.skills().stream()
						.anyMatch(skill ->  jobTypeIdSearch==0 || skill.jobTypeId() == jobTypeIdSearch))
				.toList();
		return soundMakers.size();
	}

	public List<ArtistResponse> findByArtistNameSoundCheckers(String artistName, int page) {
		return repo.findByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType.SOUND_CHECKER, artistName, PageRequest.of(page, 3))
		.stream()
		.map(artist -> {
			var userResponse = userClient.findUserById(artist.getUserId());
			
			var skill = skillRepo.findByArtistId(artist.getId());
			if(skill == null) {
				throw new SkillException("No Skill with artistId :" + artist.getId());
			}
			
			var jobTypeResponseList = new ArrayList<JobTypeResponse>();
			for (Long jobTypeId : skill.getJobTypeIds()) {
				var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
				jobTypeResponseList.add(jobTypeResponse);
			}
			
			return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
		})
		.toList();
	}
	
	public long countByArtistNameSoundCheckers(String artistName) {
		return repo.countByArtistTypeAndArtistNameContainingIgnoreCase(ArtistType.SOUND_CHECKER, artistName);
	}

	public ArtistResponse findByUserId(String userId) {
		Artist artist = repo.findByUserId(userId);
		
		if(artist != null) {
			var userResponse = userClient.findUserById(artist.getUserId());
			
			var skill = skillRepo.findByArtistId(artist.getId());
			if(skill == null) {
				throw new SkillException("No Skill with artistId :" + artist.getId());
			}
			
			var jobTypeResponseList = new ArrayList<JobTypeResponse>();
			for (Long jobTypeId : skill.getJobTypeIds()) {
				var jobTypeResponse = jobTypeClient.findJobTypeById(jobTypeId);
				jobTypeResponseList.add(jobTypeResponse);
			}
			
			return mapper.toArtistResponse(artist, userResponse, skillMapper.toSkillResponseList(jobTypeResponseList));
		}else {
			return null;
		}
	}
	
	
}
