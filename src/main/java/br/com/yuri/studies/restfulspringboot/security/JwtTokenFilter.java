package br.com.yuri.studies.restfulspringboot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = Logger.getLogger(JwtTokenFilter.class.getName());

	private final JwtTokenService tokenProvider;

	@Autowired
	public JwtTokenFilter(JwtTokenService tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = tokenProvider.resolveToken(request.getHeader("Authorization"));
		try {
			Authentication auth = tokenProvider.getAuthenticationFromAccessToken(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception e) {
			LOGGER.info("Athentication filter error: " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}
