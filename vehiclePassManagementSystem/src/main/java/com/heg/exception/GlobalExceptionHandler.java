package com.heg.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	  @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {

	        ApiError error = new ApiError(
	                HttpStatus.NOT_FOUND.value(),
	                "NOT_FOUND",
	                ex.getMessage());

	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	    }

	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {

	        ApiError error = new ApiError(
	                HttpStatus.BAD_REQUEST.value(),
	                "BAD_REQUEST",
	                ex.getMessage());

	        return ResponseEntity.badRequest().body(error);
	    }

	    @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

	        ApiError error = new ApiError(
	                HttpStatus.CONFLICT.value(),
	                "DATABASE_CONFLICT",
	                "Database constraint violation.");

	        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiError> handleException(Exception ex) {

	        ApiError error = new ApiError(
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                "INTERNAL_SERVER_ERROR",
	                "An unexpected error occurred.");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	    }
}