package com.hokhanh.web.beat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BeatRequest(
	Long id,
	
	@NotNull(message = "Beat beatGenreId is required")
	Long beatGenreId,
	
	@NotBlank(message = "Beat beatName is required")
	String beatName,
	
	@NotBlank(message = "Beat beatFile is required")
	String beatFile
) {

}
