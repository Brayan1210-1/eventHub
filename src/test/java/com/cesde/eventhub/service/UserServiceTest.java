package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cesde.eventhub.dto.LoginDTO;
import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Client;
import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.UserRoles;
import com.cesde.eventhub.exception.custom.*;
import com.cesde.eventhub.mapper.ClientMapper;
import com.cesde.eventhub.mapper.UserMapper;
import com.cesde.eventhub.repository.ClientRepository;
import com.cesde.eventhub.repository.RoleRepository;
import com.cesde.eventhub.repository.UserRepository;



import java.util.HashSet;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Spy
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @InjectMocks
    private UserService userService;


    private User user;
    private UserRegisterDTO userRegisterDTO;
    private LoginDTO loginDTO;
    private Role clientRole;
    private UUID userId;
    private final String EMAIL_TEST = "test@cesde.net";
    private final String PASS_ENCODED = "password_encriptada";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail(EMAIL_TEST);
        user.setPassword(PASS_ENCODED);
        user.setRoles(new HashSet<>());
        user.setActive(true);

        clientRole = new Role();
        clientRole.setNameRole(UserRoles.CLIENTE);

       
        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail(EMAIL_TEST);
        userRegisterDTO.setPassword("password123");
        userRegisterDTO.setDocument("101010");
        userRegisterDTO.setPhone("3001234567");

        loginDTO = new LoginDTO(EMAIL_TEST, "password123");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
    
    @Test
    void validateData_ShouldThrowException_WhenEmailAlreadyExists() {
       
        when(userRepository.existsByEmail(EMAIL_TEST)).thenReturn(true);

       
        InvalidRegistration exception = assertThrows(InvalidRegistration.class, () -> {
            userService.validateData(userRegisterDTO);
        });

        assertEquals("El usuario ya existe con ese correo", exception.getMessage());
    }
    
    @Test
    void validateData_ShouldThrowException_WhenPhoneAlreadyExists() {
       
        when(clientRepository.existsByDocument(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientRepository.existsByPhone(userRegisterDTO.getPhone())).thenReturn(true);

        assertThrows(InvalidRegistration.class, () -> {
            userService.validateData(userRegisterDTO);
        });
    }
  
    @Test
    void findByEmail_ShouldThrowDataNotFound_WhenUserDoesNotExist() {
       
        String emailInexistente = "fantasma@test.com";
        when(userRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

     
        DataNotFound exception = assertThrows(DataNotFound.class, () -> {
            userService.findByEmail(emailInexistente);
        });

        assertTrue(exception.getMessage().contains("No se encontró el usuario con ese email"));
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findById_ShouldThrowDataNotFound_WhenIdDoesNotExist() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(DataNotFound.class, () -> userService.findById(randomId));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        when(userRepository.findByEmail(EMAIL_TEST)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(EMAIL_TEST);

        assertNotNull(result);
        assertEquals(EMAIL_TEST, result.getEmail());
    }
    
    @Test
    void createClient_ShouldRegisterSuccessfully() {
        when(clientRepository.existsByDocument(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientRepository.existsByPhone(anyString())).thenReturn(false);
        when(roleRepository.findByNameRole(UserRoles.CLIENTE)).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode(anyString())).thenReturn(PASS_ENCODED);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.createClient(userRegisterDTO);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowException_WhenDocumentExists() {
        when(clientRepository.existsByDocument(userRegisterDTO.getDocument())).thenReturn(true);

        assertThrows(InvalidRegistration.class, () -> userService.createClient(userRegisterDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void iniciarSesion_ShouldReturnResponse_WhenCredentialsAreCorrect() {
        when(userRepository.findByEmail(EMAIL_TEST)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);

        UserResponseDTO result = userService.iniciarSesion(loginDTO);

        assertNotNull(result);
        assertEquals(EMAIL_TEST, result.getEmail());
    }

    @Test
    void iniciarSesion_ShouldThrowNotMatch_WhenPasswordIsIncorrect() {
        when(userRepository.findByEmail(EMAIL_TEST)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(PASS_ENCODED))).thenReturn(false);

        assertThrows(NotMatch.class, () -> userService.iniciarSesion(loginDTO));
    }

    @Test
    void validateAuthority_ShouldAllow_WhenUserIsOwner() {
        mockSecurityContext(userId, "ROLE_CLIENTE");
        assertDoesNotThrow(() -> userService.validateAuthority(userId));
    }

    @Test
    void validateAuthority_ShouldAllow_WhenUserIsAdmin() {
        mockSecurityContext(UUID.randomUUID(), "ROLE_ADMIN");
        assertDoesNotThrow(() -> userService.validateAuthority(userId));
    }

    @Test
    void validateAuthority_ShouldThrowUnauthorized_WhenNotAllowed() {
        mockSecurityContext(UUID.randomUUID(), "ROLE_CLIENTE");
        assertThrows(Unauthorized.class, () -> userService.validateAuthority(userId));
    }

    private void mockSecurityContext(UUID userId, String role) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(authentication.getName()).thenReturn(userId.toString());
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        doReturn(authorities).when(authentication).getAuthorities();
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
