package com.cesde.eventhub.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); 
    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	             .csrf(csrf -> csrf.disable())
	             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            		 )
	             
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers(HttpMethod.POST, "/api/v1/autenticacion/**").permitAll()
	                .requestMatchers( "/api/v1/admin/**").hasRole("ADMIN")
	                .requestMatchers("/api/v1/zonas/**").hasRole("ADMIN")
	                .requestMatchers(
	                        "/swagger/**",
	                        "/swagger-ui/**",
	                        "/docs/**").permitAll()
	                
	                .anyRequest().authenticated())
	            
	           .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);	           

	        return http.build();
	    }

	    


}
