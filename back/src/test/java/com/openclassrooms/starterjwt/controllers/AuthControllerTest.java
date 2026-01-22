package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("encodedPassword");
        testUser.setAdmin(false);

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .build();
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Devrait authentifier un utilisateur avec succès")
        void authenticateUser_Success() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("test@test.com");
            loginRequest.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

            // Act
            ResponseEntity<?> response = authController.authenticateUser(loginRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(JwtResponse.class);
            
            JwtResponse jwtResponse = (JwtResponse) response.getBody();
            assertThat(jwtResponse.getToken()).isEqualTo("jwt-token");
            assertThat(jwtResponse.getId()).isEqualTo(1L);
            assertThat(jwtResponse.getUsername()).isEqualTo("test@test.com");
            assertThat(jwtResponse.getFirstName()).isEqualTo("John");
            assertThat(jwtResponse.getLastName()).isEqualTo("Doe");
            assertThat(jwtResponse.getAdmin()).isFalse();

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtUtils).generateJwtToken(authentication);
        }

        @Test
        @DisplayName("Devrait authentifier un admin avec succès")
        void authenticateUser_Admin_Success() {
            // Arrange
            testUser.setAdmin(true);
            
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("admin@test.com");
            loginRequest.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

            // Act
            ResponseEntity<?> response = authController.authenticateUser(loginRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            JwtResponse jwtResponse = (JwtResponse) response.getBody();
            assertThat(jwtResponse.getAdmin()).isTrue();
        }

        @Test
        @DisplayName("Devrait gérer un utilisateur non trouvé dans le repository")
        void authenticateUser_UserNotFound() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("unknown@test.com");
            loginRequest.setPassword("password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
            when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

            // Act
            ResponseEntity<?> response = authController.authenticateUser(loginRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            JwtResponse jwtResponse = (JwtResponse) response.getBody();
            assertThat(jwtResponse.getAdmin()).isFalse();
        }
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Devrait enregistrer un nouvel utilisateur avec succès")
        void registerUser_Success() {
            // Arrange
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail("newuser@test.com");
            signupRequest.setFirstName("Jane");
            signupRequest.setLastName("Doe");
            signupRequest.setPassword("password123");

            when(userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // Act
            ResponseEntity<?> response = authController.registerUser(signupRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
            
            MessageResponse messageResponse = (MessageResponse) response.getBody();
            assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");

            verify(userRepository).existsByEmail("newuser@test.com");
            verify(passwordEncoder).encode("password123");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Devrait retourner une erreur si l'email existe déjà")
        void registerUser_EmailAlreadyExists() {
            // Arrange
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail("existing@test.com");
            signupRequest.setFirstName("Jane");
            signupRequest.setLastName("Doe");
            signupRequest.setPassword("password123");

            when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

            // Act
            ResponseEntity<?> response = authController.registerUser(signupRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
            
            MessageResponse messageResponse = (MessageResponse) response.getBody();
            assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");

            verify(userRepository).existsByEmail("existing@test.com");
            verify(userRepository, never()).save(any(User.class));
        }
    }
}

