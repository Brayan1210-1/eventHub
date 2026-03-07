package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cesde.eventhub.dto.response.ResponseLoginDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private JwtService jwtService;
    @Mock private UserService userService;
    @Spy private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User userEntity;
    private UserResponseDTO userDTO;
    private RefreshToken refreshTokenEntity;
    private final String TOKEN_STR = "mock-refresh-token";

    @BeforeEach
    void setUp() {
        // 1. Inyectar el valor de expiración
        ReflectionTestUtils.setField(refreshTokenService, "jwtRefresh", 86400000L);

        // 2. Preparar datos
        UUID userId = UUID.randomUUID();
        
        userEntity = new User();
        userEntity.setId(userId);
        userEntity.setEmail("test@cesde.net");
        userEntity.setRoles(new HashSet<>());

        userDTO = new UserResponseDTO();
        userDTO.setId(userId);
        userDTO.setEmail("test@cesde.net");

        refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(TOKEN_STR);
        refreshTokenEntity.setUser(userEntity);
        // Ponemos una fecha futura para que no esté expirado por defecto
        refreshTokenEntity.setExpirationDate(Instant.now().plusSeconds(3600));
    }

    @Test
    void createRefreshToken_ShouldSaveAndReturnToken() {
        // Arrange
        when(userService.findByEmail(userDTO.getEmail())).thenReturn(userEntity);
        when(jwtService.generateRefreshToken(userDTO.getId())).thenReturn(TOKEN_STR);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshTokenEntity);

        // Act
        RefreshToken result = refreshTokenService.createRefreshToken(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TOKEN_STR, result.getToken());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void renovateAccessToken_ShouldRotateAndReturnNewTokens() {
        // Arrange
        // Mock de validación inicial
        when(refreshTokenRepository.findByToken(TOKEN_STR)).thenReturn(Optional.of(refreshTokenEntity));
        
        // Mock de la rotación (createRefreshToken se llama internamente)
        when(userService.findByEmail(anyString())).thenReturn(userEntity);
        when(jwtService.generateRefreshToken(any())).thenReturn("new-refresh-token");
        
        // Mock del nuevo Access Token
        when(jwtService.generateAccessToken(any(), any(), any())).thenReturn("new-access-token");
        
        // Necesitamos que el save devuelva algo para que no falle el flujo
        RefreshToken newRefresh = new RefreshToken();
        newRefresh.setToken("new-refresh-token");
        when(refreshTokenRepository.save(any())).thenReturn(newRefresh);

        // Act
        ResponseLoginDTO result = refreshTokenService.renovateAccessToken(TOKEN_STR);

        // Assert
        assertNotNull(result);
        assertEquals("new-refresh-token", result.getRefreshToken());
        assertEquals("new-access-token", result.getAccessToken());
        verify(refreshTokenRepository).deleteByUsuario(any());
    }

    @Test
    void renovateAccessToken_ShouldThrowException_WhenTokenNotFound() {
        // Arrange
        when(refreshTokenRepository.findByToken("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            refreshTokenService.renovateAccessToken("invalid");
        });
    }

    @Test
    void renovateAccessToken_ShouldThrowException_WhenTokenExpired() {
        // Arrange: Forzamos la expiración
        refreshTokenEntity.setExpirationDate(Instant.now().minusSeconds(10));
        when(refreshTokenRepository.findByToken(TOKEN_STR)).thenReturn(Optional.of(refreshTokenEntity));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            refreshTokenService.renovateAccessToken(TOKEN_STR);
        });
        verify(refreshTokenRepository).deleteByexpirationDate();
    }
}