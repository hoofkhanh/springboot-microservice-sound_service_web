package com.hokhanh.web.skill;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillRepository extends MongoRepository<Skill, String> {

	Skill findByArtistId(String artistId);
}
