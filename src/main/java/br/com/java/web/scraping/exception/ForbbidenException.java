package br.com.java.web.scraping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class ForbbidenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForbbidenException() {
		super();
	}
	
	public ForbbidenException(String message) {
		super(message);
	}
	
	public ForbbidenException(Throwable cause) {
		super(cause);
	}
	
	public ForbbidenException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
