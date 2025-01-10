package com.apibuilder.dev.apibuilder.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtService {
	
	private static final String SECRET="ALSDJKFALHSALKDFHAJKSDF454446ERH34563B456LK435U3H45RFS2345HDRAGFKERGE";
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
 
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(generateKey()).build().parseClaimsJws(token).getBody();
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims=new HashMap<>();
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(generateKey())
				.setExpiration(new Date(System.currentTimeMillis()+60*60*1000)).compact();
	}
	
	public Key generateKey() {
		byte[] keyCode=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyCode);
	}
	
	public String extractSubject(String token) {
		return extractClaim(token,Claims::getSubject);
	}

	public boolean isTokenExpired(String token) {
		return extractClaim(token,Claims::getExpiration).before(new Date());
	}
	
    public boolean validateToken(String token, UserDetails userDetails) {
    	String username=extractSubject(token);
    	return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }
}