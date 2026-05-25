package com.cesde.eventhub.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.exception.custom.NotMatch;
import com.cesde.eventhub.exception.custom.Unauthorized;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = InvalidRegistration.class)
	public ResponseEntity<ErrorResponse> invalidUserRegistrer(InvalidRegistration ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
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
	
	@ExceptionHandler(exception = InternalError.class)
	public ResponseEntity<ErrorResponse> internalError(InternalError ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(exception = Unauthorized.class)
	public ResponseEntity<ErrorResponse> unauthorizedError(Unauthorized ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	
}
