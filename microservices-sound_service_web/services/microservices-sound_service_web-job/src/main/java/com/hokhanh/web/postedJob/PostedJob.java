package com.hokhanh.web.postedJob;


import java.time.LocalDate;

import com.hokhanh.web.jobType.JobType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posted_jobs")
@Builder
@Data
public class PostedJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String posterId;
	
	@ManyToOne
	@JoinColumn(name = "job_type_id")
	private JobType jobType;
	
	private String topic;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	private String trackFile;
	private LocalDate postDate;
}
