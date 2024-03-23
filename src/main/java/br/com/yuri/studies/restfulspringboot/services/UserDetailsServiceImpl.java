package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class.getName());

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.log(Level.INFO, "Finding a User by name {0}!", username);

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
	}
}
