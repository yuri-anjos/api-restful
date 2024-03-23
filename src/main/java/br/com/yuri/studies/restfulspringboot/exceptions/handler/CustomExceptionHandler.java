package br.com.yuri.studies.restfulspringboot.exceptions.handler;

import br.com.yuri.studies.restfulspringboot.exceptions.ExceptionResponse;
import br.com.yuri.studies.restfulspringboot.exceptions.ResourceNotFoundException;
import br.com.yuri.studies.restfulspringboot.exceptions.RuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest request) {
		var exceptionResponse = new ExceptionResponse(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(Exception exception, WebRequest request) {
		var exceptionResponse = new ExceptionResponse(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RuleException.class)
	public final ResponseEntity<ExceptionResponse> handleRuleException(Exception ex, WebRequest request) {
		var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(Exception ex, WebRequest request) {
		var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleAuthenticationException(Exception exception, WebRequest request) {
		var exceptionResponse = new ExceptionResponse(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
}
