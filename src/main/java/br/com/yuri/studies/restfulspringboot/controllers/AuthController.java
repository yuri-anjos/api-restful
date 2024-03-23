package br.com.yuri.studies.restfulspringboot.controllers;

import br.com.yuri.studies.restfulspringboot.dtos.UserDTO;
import br.com.yuri.studies.restfulspringboot.dtos.security.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.dtos.security.TokenDTO;
import br.com.yuri.studies.restfulspringboot.exceptions.RuleException;
import br.com.yuri.studies.restfulspringboot.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints that manages Authentication")
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(
			summary = "Signin",
			description = "Authenticates a user and returns a token"
	)
	@PostMapping("/signin")
	public TokenDTO signin(@RequestBody @Valid AccountCredentialsDTO accountCredentials) {
		return authService.signin(accountCredentials);
	}

	@Operation(
			summary = "Signup",
			description = "Creates a user and returns a token"
	)
	@PostMapping("/signup")
	public ResponseEntity<TokenDTO> signup(@RequestBody @Valid UserDTO userDTO) {
		var token = authService.signup(userDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(token);
	}

	@Operation(
			summary = "Refresh Token",
			description = "Authenticates a user by passing username and refresh token"
	)
	@PostMapping("/refresh")
	public TokenDTO refreshToken(@RequestHeader("Authorization") String refreshToken) {
		if (!StringUtils.hasText(refreshToken)) {
			throw new RuleException("Invalid client request");
		}

		return authService.refreshToken(refreshToken);
	}
}
