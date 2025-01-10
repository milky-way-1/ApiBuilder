package com.apibuilder.dev.apibuilder.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apibuilder.dev.apibuilder.model.User;
import com.apibuilder.dev.apibuilder.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userFound=userRepository.findByEmail(username);
		if(userFound.isPresent()) {
			return new CustomUserDetails(userFound.get());
		}
		return null;
	}

}
