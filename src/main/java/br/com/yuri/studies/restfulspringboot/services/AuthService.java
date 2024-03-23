package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.dtos.UserDTO;
import br.com.yuri.studies.restfulspringboot.dtos.security.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.dtos.security.TokenDTO;
import br.com.yuri.studies.restfulspringboot.exceptions.RuleException;
import br.com.yuri.studies.restfulspringboot.models.User;
import br.com.yuri.studies.restfulspringboot.repositories.UserRepository;
import br.com.yuri.studies.restfulspringboot.security.JwtTokenService;
import br.com.yuri.studies.restfulspringboot.util.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final JwtTokenService tokenProvider;

	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public AuthService(JwtTokenService tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public TokenDTO signin(AccountCredentialsDTO accountCredentials) {
		var username = accountCredentials.getUsername();
		var password = accountCredentials.getPassword();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception ex) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}

		var user = userRepository.findByUsername(username).get();
		return tokenProvider.createTokenDTO(username, user.getRoles());
	}

	public TokenDTO signup(UserDTO userDTO) {
		if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
			throw new RuleException("The \"password\" and \"confirmPassword\" must be the same!");
		}

		var encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

		var user = new User();
		user.setPassword(encryptedPassword);
		user.setFullname(userDTO.getFullname());
		user.setUsername(userDTO.getUsername());
		user.addPermission(PermissionEnum.COMMON_USER);
		user = userRepository.save(user);

		return tokenProvider.createTokenDTO(user.getUsername(), user.getRoles());
	}

	public TokenDTO refreshToken(String refreshToken) {
		return tokenProvider.refreshToken(refreshToken);
	}
}
