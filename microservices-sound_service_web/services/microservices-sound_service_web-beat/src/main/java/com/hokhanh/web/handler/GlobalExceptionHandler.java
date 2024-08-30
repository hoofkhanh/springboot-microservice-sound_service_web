package com.hokhanh.web.handler;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hokhanh.web.exception.PostedBeatException;
import com.hokhanh.web.exception.BeatGenreException;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BeatGenreException.class)
	public ResponseEntity<String> handle(BeatGenreException exp){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(exp.getMsg());
	}
	
	@ExceptionHandler(PostedBeatException.class)
	public ResponseEntity<String> handle(PostedBeatException exp){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(exp.getMsg());
	}
	
	@ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException e) {
		return ResponseEntity
				.status(e.status())
				.body(e.getMessage());
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp){
		var errors = new HashMap<String, String>();
		
		exp.getBindingResult().getAllErrors()
							.forEach(error -> {
								var fieldName = ((FieldError)error).getField();
								var errorMessage = error.getDefaultMessage();
								errors.put(fieldName, errorMessage);
							});
		
		return ResponseEntity	
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(errors));
	}
}
