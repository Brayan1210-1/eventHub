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

	 @Value("${jwt.access-expiration}")
	 private long jwtAccess; 
	 
	 @Value("${jwt.refresh-expiration}")
	 private long jwtRefresh;
	 
	 public String generarAccessToken(Long id, String email, String rol, String nombre) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("id", id);
	        claims.put("email", email);
	        claims.put("rol", rol);
	        claims.put("nombre", nombre);

	        return Jwts.builder()
	        		.claims(claims) 
	                .subject(email) 
	                .issuedAt(new Date(System.currentTimeMillis()))  
	                .expiration(new Date(System.currentTimeMillis() + jwtAccess)) 
	                .signWith(getSignInKey()) 
	                .compact();
	    }
	 
	 public String generarRefreshToken(String email) {
		 
		 return Jwts.builder()
	                .subject(email) 
	                .issuedAt(new Date(System.currentTimeMillis()))  
	                .expiration(new Date(System.currentTimeMillis() + jwtRefresh)) 
	                .signWith(getSignInKey()) 
	                .compact();
		 
	 }
	 
		 
	 public String extraerEmail(String token) {
	        return extractAllClaims(token).getSubject();
	    }
	 
	 public boolean tokenEsValido(String token, UserDetails userDetails) {
	        final String username = extraerEmail(token);
	        return (username.equals(userDetails.getUsername()) && !tokenEstaExpirado(token));
	    }
	 
	 private boolean tokenEstaExpirado(String token) {
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
