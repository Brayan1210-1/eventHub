package com.cesde.eventhub.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class JwtService {

	 @Value("${jwt.secret}")
	 private String secretKey; 

	 @Value("${jwt.access-expiration}")
	 private long jwtAccess; 
	 
	 @Value("${jwt.refresh-expiration}")
	 private long jwtRefresh;
	 
	 public String generateAccessToken(UUID id, String email, Collection<String> roles) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("id", id);
	        claims.put("email", email);
	        claims.put("roles", roles);
	        
	        
	        String accessToken = Jwts.builder()
	        		.header()
	        		.add("typ", "JWT")
	        		.and()
	        		
	        		.claims(claims) 
	                .subject(id.toString()) 
	                .issuedAt(new Date(System.currentTimeMillis()))  
	                .expiration(new Date(System.currentTimeMillis() + jwtAccess)) 
	                .signWith(getSignInKey()) 
	                .compact();
	        
	        return accessToken;
	    }
	 
	 public String generateRefreshToken(UUID id) {
		 
		 String refreshToken =Jwts.builder()
				 
	                .header()
	                .add("typ", "JWT")
	                .and()
				 
	                .subject(id.toString()) 
	                .issuedAt(new Date(System.currentTimeMillis()))  
	                .expiration(new Date(System.currentTimeMillis() + jwtRefresh)) 
	                .signWith(getSignInKey()) 
	                .compact();
		 
		 return refreshToken;
		 
	 }
	 
		 
	 public String extractId(String token) {
	        return extractAllClaims(token).getSubject();
	    }
	 
	 public boolean tokenIsValid(String token, UserDetails userDetails) {
	        final String username = extractId(token);
	        return (username.equals(userDetails.getUsername()) && !tokenIsExpired(token));
	    }
	 
	 private boolean tokenIsExpired(String token) {
	        return extractAllClaims(token).getExpiration().before(new Date());
	    }
	 
	 private Claims extractAllClaims(String token) {
	        return Jwts.parser() 
	                .verifyWith(getSignInKey()) 
	                .build()
	                .parseSignedClaims(token) 
	                .getPayload(); 
	    }
	 
	 private SecretKey getSignInKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

}
