package com.cesde.eventhub.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidUserRegistration;
import com.cesde.eventhub.exception.custom.NotMatch;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = InvalidUserRegistration.class)
	public ResponseEntity<ErrorResponse> invalidUserRegistrer(InvalidUserRegistration ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(exception = DataNotFound.class)
	public ResponseEntity<ErrorResponse> dataNotFound(DataNotFound ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(exception = NotMatch.class)
	public ResponseEntity<ErrorResponse> notMatch(NotMatch ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
	}
	
	
	
	
	
	
	
}
