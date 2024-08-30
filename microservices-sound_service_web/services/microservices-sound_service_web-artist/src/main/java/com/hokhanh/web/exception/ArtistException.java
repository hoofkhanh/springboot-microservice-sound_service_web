package com.hokhanh.web.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistException extends RuntimeException {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private final String msg;

}
