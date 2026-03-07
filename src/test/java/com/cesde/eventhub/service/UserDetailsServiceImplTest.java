package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User userEntity;
    private final UUID userId = UUID.randomUUID();
    private final String userEmail = "remate@eventhub.com";

    @BeforeEach
    void setUp() {
      
        Role role = new Role();
        role.setNameRole(UserRoles.CLIENTE);

        userEntity = new User();
        userEntity.setId(userId);
        userEntity.setEmail(userEmail);
        userEntity.setPassword("encoded_password");
        userEntity.setRoles(new HashSet<>(List.of(role)));
    }

    @Test
    void loadUserByUsername_ShouldLoadByUUID_WhenIdentifierIsUUID() {
       
        String uuidString = userId.toString();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        
        UserDetails result = userDetailsService.loadUserByUsername(uuidString);

        
        assertNotNull(result);
        assertEquals(uuidString, result.getUsername());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void loadUserByUsername_ShouldLoadByEmail_WhenIdentifierIsNotUUID() {
     
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));

       
        UserDetails result = userDetailsService.loadUserByUsername(userEmail);

        assertNotNull(result);
        assertEquals(userId.toString(), result.getUsername());
        verify(userRepository).findByEmail(userEmail);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void loadUserByUsername_ShouldThrowDataNotFound_WhenUserNotFoundById() {
       
        String uuidString = userId.toString();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DataNotFound.class, () -> userDetailsService.loadUserByUsername(uuidString));
    }

    @Test
    void loadUserByUsername_ShouldThrowDataNotFound_WhenUserNotFoundByEmail() {
      
        String randomEmail = "noexiste@test.com";
        when(userRepository.findByEmail(randomEmail)).thenReturn(Optional.empty());

        assertThrows(DataNotFound.class, () -> userDetailsService.loadUserByUsername(randomEmail));
    }
}