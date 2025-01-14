package com.apibuilder.dev.apibuilder.service;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apibuilder.dev.apibuilder.dto.request.LoginRequest;
import com.apibuilder.dev.apibuilder.dto.request.UserRegistrationRequest;
import com.apibuilder.dev.apibuilder.dto.response.AuthResponse;
import com.apibuilder.dev.apibuilder.dto.response.UserResponse;
import com.apibuilder.dev.apibuilder.model.User;
import com.apibuilder.dev.apibuilder.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}

	public User getUserByEmail(String email) {
		Optional<User> userFoundOptional=userRepository.findByEmail(email);
		if(userFoundOptional.isPresent()) {
			return userFoundOptional.get();
		}
		return null;
	}
	
	
	public User updatedUser(User user) {
		User userFound=userRepository.findByEmail(user.getEmail()).get();
		if(userFound!=null) {
			userRepository.save(user);
		}
		return null;
	}
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	public UserResponse getUserProfile(String userId) {
		User userFound=userRepository.findById(userId).get();
		UserResponse userResponse=new UserResponse();
		BeanUtils.copyProperties(userFound, userResponse);
		return userResponse;
	}

	public UserResponse addNewuser(@Valid UserRegistrationRequest userRequest) {
		User user=new User();
		BeanUtils.copyProperties(userRequest, user);
		user.setPassword(encoder.encode(userRequest.getPassword()));
                user.setCreatedAt(new Date()); 
		user.setUpdatedAt(new Date());
		User userCreated=userRepository.save(user);
		UserResponse userResponse=new UserResponse();
		BeanUtils.copyProperties(userCreated, userResponse);
		return userResponse;
	}
	
	public AuthResponse login(LoginRequest loginRequest) {
		AuthResponse authResponse=new AuthResponse();
		
		Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()))	;
		if(auth.isAuthenticated()) {
			authResponse.setAuthenticated(true);
			String token=jwtService.generateToken(loginRequest.getEmail());
			authResponse.setToken(token);
			User userFound=userRepository.findByEmail(loginRequest.getEmail()).get();
			authResponse.setUserId(userFound.getId());
		}
		return authResponse;
	}
}
