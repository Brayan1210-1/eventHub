package com.cesde.eventhub.servicio;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtServicio {

	 @Value("${jwt.secret}")
	 private String secretKey; 

	 @Value("${jwt.expiration}")
	 private long jwtExpiration; 
	 
	 public String generateToken(Long id, String email, String rol, String nombre) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("id", id);
	        claims.put("email", email);
	        claims.put("rol", rol);
	        claims.put("nombre", nombre);

	        return Jwts.builder()
	        		.claims(claims) 
	                .subject(email) 
	                .issuedAt(new Date(System.currentTimeMillis()))  
	                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) 
	                .signWith(getSignInKey()) 
	                .compact();
	    }
	 
	 public String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	    }
	 
	 public boolean isTokenValid(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	 
	 private boolean isTokenExpired(String token) {
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
