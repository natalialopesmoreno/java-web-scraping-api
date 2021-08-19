package br.com.java.web.scraping.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.common.net.HttpHeaders;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<StandardError> badRequestException(BadRequestException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new StandardError(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<StandardError> unauthorizedException(UnauthorizedException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new StandardError(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(ForbbidenException.class)
	public ResponseEntity<StandardError> forbbidenException(ForbbidenException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new StandardError(HttpStatus.FORBIDDEN, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<StandardError> notFoundException(NotFoundException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new StandardError(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<StandardError> internalServerErrorException(InternalServerErrorException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new StandardError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<StandardError> runtimeException(RuntimeException e, HttpServletRequest request ) {
	
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new StandardError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI()));
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handlerException(Exception e , Object body, HttpStatus httpStatus, HttpHeaders headers, WebRequest request){
		return ResponseEntity.status(httpStatus)
				.body(new StandardError(httpStatus, e.getMessage(), request.getContextPath()));
	}
	

}
