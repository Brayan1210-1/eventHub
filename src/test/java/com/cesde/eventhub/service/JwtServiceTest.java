package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final UUID userId = UUID.randomUUID();
    private final String email = "test@cesde.net";
    private final List<String> roles = List.of("ROLE_CLIENTE");
    
    private final String testSecret = "ZmFsdGEgbXVjaG8gcGFyYSBlbCA2MCBwZXJvIHlhIGNhc2kgbG8gbG9ncmFtb3MgY29tcGE=";

    @BeforeEach
    void setUp() {
       
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtAccess", 3600000L); // 1 hora
        ReflectionTestUtils.setField(jwtService, "jwtRefresh", 86400000L); // 1 día
    }

    @Test
    void generateAccessToken_ShouldCreateValidToken() {
        
        String token = jwtService.generateAccessToken(userId, email, roles);

       
        assertNotNull(token);
        assertEquals(userId.toString(), jwtService.extractId(token));
    }

    @Test
    void generateRefreshToken_ShouldCreateValidToken() {
        
        String token = jwtService.generateRefreshToken(userId);

       
        assertNotNull(token);
        assertEquals(userId.toString(), jwtService.extractId(token));
    }

    @Test
    void tokenIsValid_ShouldReturnTrue_WhenTokenCorrect() {
        // Arrange
        String token = jwtService.generateAccessToken(userId, email, roles);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(userId.toString());

        // Act
        boolean isValid = jwtService.tokenIsValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void tokenIsValid_ShouldReturnFalse_WhenUserDoesNotMatch() {
        String token = jwtService.generateAccessToken(userId, email, roles);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otro-uuid");

       
        boolean isValid = jwtService.tokenIsValid(token, userDetails);

      
        assertFalse(isValid);
    }
}