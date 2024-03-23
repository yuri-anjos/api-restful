package br.com.yuri.studies.restfulspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtAutheticationException extends AuthenticationException implements Serializable {

	@Serial
	private static final long serialVersionUID = 4900799794038027742L;

	public InvalidJwtAutheticationException(String msg) {
		super(msg);
	}
}
