package com.hokhanh.web.skill;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "skills")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill {

	@Id
	private String id;
	
	private String artistId;
	private List<Long> jobTypeIds;
}
