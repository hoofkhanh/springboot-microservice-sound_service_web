package com.hokhanh.web.beat;


import com.hokhanh.web.beatGenre.BeatGenre;
import com.hokhanh.web.postedBeat.PostedBeat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "beats")
@Builder
@Data
public class Beat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "beat_genre_id")
	private BeatGenre beatGenre;
	
	private String beatName;
	private String beatFile;
	
	@OneToOne(mappedBy = "beat")
	private PostedBeat postedBeat;
	
	
}
