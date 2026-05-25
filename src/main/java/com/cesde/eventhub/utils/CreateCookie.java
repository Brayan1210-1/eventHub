package com.cesde.eventhub.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CreateCookie {

	public static void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);   
        cookie.setSecure(false);  
        cookie.setPath("/");        
        cookie.setMaxAge(7 * 24 * 60 * 60); 
        
        response.addCookie(cookie);
    }
}
