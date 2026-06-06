package com.cesde.eventhub.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie; 

import jakarta.servlet.http.HttpServletResponse;

public class CreateCookie {

	public static void setRefreshTokenCookie(HttpServletResponse response, String token) {
	   
	    ResponseCookie cookie = ResponseCookie.from("refresh_token", token)
	            .httpOnly(true)
	            .secure(true)       
	            .sameSite("None")   
	            .path("/")
	            .maxAge(7 * 24 * 60 * 60)
	            .build();
	    
	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	public static void deleteRefreshTokenCookie(HttpServletResponse response) {
	    ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
	            .httpOnly(true)
	            .secure(true)
	            .sameSite("None")   
	            .path("/")
	            .maxAge(0)          
	            .build();
	    
	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
