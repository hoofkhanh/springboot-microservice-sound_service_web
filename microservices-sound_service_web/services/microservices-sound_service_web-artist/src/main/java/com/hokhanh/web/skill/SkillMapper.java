package com.hokhanh.web.skill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hokhanh.web.jobType.JobTypeResponse;

@Service
public class SkillMapper {

	public Skill toSkill(String artistId, List<Long> jobTypeIds) {
		return Skill.builder()
				.artistId(artistId)
				.jobTypeIds(jobTypeIds)
				.build();
	}

	public List<SkillResponse> toSkillResponseList(ArrayList<JobTypeResponse> jobTypeResponseList) {
		var skillResponses = new ArrayList<SkillResponse>();
		for (JobTypeResponse jobTypeResponse : jobTypeResponseList) {
			skillResponses.add(new SkillResponse(jobTypeResponse.id(), jobTypeResponse.name()));
		}
		
		return skillResponses;
	}
	
	

}
