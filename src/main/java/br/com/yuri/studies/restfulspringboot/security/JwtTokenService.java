package br.com.yuri.studies.restfulspringboot.security;

import br.com.yuri.studies.restfulspringboot.dtos.security.TokenDTO;
import br.com.yuri.studies.restfulspringboot.exceptions.InvalidJwtAutheticationException;
import br.com.yuri.studies.restfulspringboot.services.UserDetailsServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {

	@Value("${security.jwt.token.secret-key}")
	private String tokenSecretKey;

	@Value("${security.jwt.token.expire-length}")
	private int tokenValidity;

	private Algorithm tokenAlgorithm;

	private final UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	public JwtTokenService(UserDetailsServiceImpl userDetailsServiceImpl) {
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	@PostConstruct
	protected void init() {
		tokenSecretKey = Base64.getEncoder().encodeToString(tokenSecretKey.getBytes());
		tokenAlgorithm = Algorithm.HMAC256(tokenSecretKey.getBytes());
	}

	public TokenDTO createTokenDTO(String username, List<String> roles) {
		var now = new Date();
		var accessTokenExpirationDate = new Date(now.getTime() + tokenValidity);
		var refreshTokenExpirationDate = new Date(now.getTime() + (tokenValidity * 3));

		var issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

		var accessToken = getAccessToken(username, roles, now, accessTokenExpirationDate, issuerUrl);
		var refreshToken = getRefreshToken(username, roles, now, refreshTokenExpirationDate, issuerUrl);

		return new TokenDTO(username, Boolean.TRUE, now, accessTokenExpirationDate, accessToken, refreshToken);
	}

	public TokenDTO refreshToken(String bearerRefreshToken) {
		var refreshToken = resolveToken(bearerRefreshToken);
		var decodedJWT = decodeAndValidateRefreshToken(refreshToken);

		var username = decodedJWT.getSubject();
		var roles = decodedJWT.getClaim("roles").asList(String.class);
		return createTokenDTO(username, roles);
	}

	public UsernamePasswordAuthenticationToken getAuthenticationFromAccessToken(String accessToken) {
		var decodedJWT = decodeAndValidateAccessToken(accessToken);
		var userDetails = userDetailsServiceImpl.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}

		return null;
	}

	private boolean isTokenValid(DecodedJWT decodedJWT) {
		return decodedJWT.getExpiresAt().after(new Date());
	}

	private DecodedJWT decodeAndValidateAccessToken(String token) {
		try {
			DecodedJWT decodedJWT = JWT.require(tokenAlgorithm).build().verify(token);
			if (!isTokenValid(decodedJWT) || Boolean.TRUE.equals(decodedJWT.getClaim("isRefreshToken").asBoolean())) {
				throw new InvalidJwtAutheticationException("");
			}
			return decodedJWT;
		} catch (Exception e) {
			throw new InvalidJwtAutheticationException("Expired or invalid JWT Access Token");
		}
	}

	private DecodedJWT decodeAndValidateRefreshToken(String token) {
		try {
			DecodedJWT decodedJWT = JWT.require(tokenAlgorithm).build().verify(token);
			if (!isTokenValid(decodedJWT) || Boolean.FALSE.equals(decodedJWT.getClaim("isRefreshToken").asBoolean())) {
				throw new InvalidJwtAutheticationException("");
			}
			return decodedJWT;
		} catch (Exception e) {
			throw new InvalidJwtAutheticationException("Expired or invalid JWT Access Token");
		}
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date expiresAt, String issuerUrl) {
		return JWT
				.create()
				.withClaim("roles", roles)
				.withClaim("isRefreshToken", Boolean.FALSE)
				.withIssuedAt(now)
				.withExpiresAt(expiresAt)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(tokenAlgorithm)
				.strip();
	}

	private String getRefreshToken(String username, List<String> roles, Date now, Date expiresAt, String issuerUrl) {
		return JWT
				.create()
				.withClaim("roles", roles)
				.withClaim("isRefreshToken", Boolean.TRUE)
				.withIssuedAt(now)
				.withExpiresAt(expiresAt)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(tokenAlgorithm)
				.strip();
	}
}
