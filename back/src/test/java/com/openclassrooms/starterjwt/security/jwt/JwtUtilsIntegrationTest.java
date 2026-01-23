package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JwtUtils Integration Tests")
class JwtUtilsIntegrationTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Devrait générer et valider un token JWT")
    void generateAndValidateToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(token).isNotNull();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
    }

    @Test
    @DisplayName("Devrait extraire le username du token")
    void getUserNameFromToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("extract@example.com")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        String token = jwtUtils.generateJwtToken(authentication);

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(username).isEqualTo("extract@example.com");
    }

    @Test
    @DisplayName("Devrait rejeter un token invalide")
    void invalidToken() {
        boolean isValid = jwtUtils.validateJwtToken("invalid.token.here");

        assertThat(isValid).isFalse();
    }
}

