package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Devrait retourner un utilisateur par son ID")
        void findById_Success() {
            when(userService.findById(1L)).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(userDto);

            ResponseEntity<?> response = userController.findById("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(userDto);
            verify(userService).findById(1L);
            verify(userMapper).toDto(user);
        }

        @Test
        @DisplayName("Devrait retourner 404 si l'utilisateur n'existe pas")
        void findById_NotFound() {
            when(userService.findById(999L)).thenReturn(null);

            ResponseEntity<?> response = userController.findById("999");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(userService).findById(999L);
            verify(userMapper, never()).toDto(any(User.class));
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void findById_BadRequest() {
            ResponseEntity<?> response = userController.findById("invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(userService, never()).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer un utilisateur avec succès")
        void delete_Success() {
            when(userService.findById(1L)).thenReturn(user);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("test@test.com");
            SecurityContextHolder.setContext(securityContext);
            doNothing().when(userService).delete(1L);

            ResponseEntity<?> response = userController.save("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userService).delete(1L);
        }

        @Test
        @DisplayName("Devrait retourner 404 si l'utilisateur n'existe pas")
        void delete_NotFound() {
            when(userService.findById(999L)).thenReturn(null);

            ResponseEntity<?> response = userController.save("999");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(userService, never()).delete(anyLong());
        }

        @Test
        @DisplayName("Devrait retourner 401 si l'utilisateur n'est pas autorisé")
        void delete_Unauthorized() {
            when(userService.findById(1L)).thenReturn(user);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("other@test.com");
            SecurityContextHolder.setContext(securityContext);

            ResponseEntity<?> response = userController.save("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            verify(userService, never()).delete(anyLong());
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void delete_BadRequest() {
            ResponseEntity<?> response = userController.save("invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(userService, never()).delete(anyLong());
        }
    }
}

