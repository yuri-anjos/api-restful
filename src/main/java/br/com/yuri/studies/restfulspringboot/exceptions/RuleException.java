package br.com.yuri.studies.restfulspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RuleException extends RuntimeException implements Serializable {

	@Serial
	private static final long serialVersionUID = -4825140442177374648L;

	public RuleException(String ex) {
		super(ex);
	}
}
