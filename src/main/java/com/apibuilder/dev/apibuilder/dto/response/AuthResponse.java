package com.apibuilder.dev.apibuilder.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
	
	String userId;
	String token;
	boolean isAuthenticated;

}
