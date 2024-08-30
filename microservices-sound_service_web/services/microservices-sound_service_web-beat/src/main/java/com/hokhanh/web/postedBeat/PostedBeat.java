package com.hokhanh.web.postedBeat;

import java.time.LocalDate;

import com.hokhanh.web.beat.Beat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posted_beats")
@Builder
@Data
public class PostedBeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "beat_id")
	private Beat beat;
	
	private String sellerId;
	private double price;
	private LocalDate postedDate;
}
