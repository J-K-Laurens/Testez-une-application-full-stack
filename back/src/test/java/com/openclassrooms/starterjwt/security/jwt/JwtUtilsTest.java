package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtils Tests")
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    private UserDetailsImpl userDetails;
    private String jwtSecret = "testSecretKeyForJwtTestingPurposesOnly123456789";
    private int jwtExpirationMs = 86400000; // 24 heures

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
    }

    @Nested
    @DisplayName("GenerateJwtToken Tests")
    class GenerateJwtTokenTests {

        @Test
        @DisplayName("Devrait générer un token JWT valide")
        void generateJwtToken_Success() {
            when(authentication.getPrincipal()).thenReturn(userDetails);

            String token = jwtUtils.generateJwtToken(authentication);

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            // Vérifie que le token peut être parsé
            String username = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            assertThat(username).isEqualTo("test@test.com");
        }
    }

    @Nested
    @DisplayName("GetUserNameFromJwtToken Tests")
    class GetUserNameFromJwtTokenTests {

        @Test
        @DisplayName("Devrait extraire le nom d'utilisateur du token")
        void getUserNameFromJwtToken_Success() {
            String token = Jwts.builder()
                    .setSubject("test@test.com")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            String username = jwtUtils.getUserNameFromJwtToken(token);

            assertThat(username).isEqualTo("test@test.com");
        }
    }

    @Nested
    @DisplayName("ValidateJwtToken Tests")
    class ValidateJwtTokenTests {

        @Test
        @DisplayName("Devrait valider un token JWT valide")
        void validateJwtToken_ValidToken() {
            String token = Jwts.builder()
                    .setSubject("test@test.com")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(token);

            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Devrait retourner false pour une signature invalide")
        void validateJwtToken_InvalidSignature() {
            String token = Jwts.builder()
                    .setSubject("test@test.com")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(token);

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner false pour un token malformé")
        void validateJwtToken_MalformedToken() {
            String malformedToken = "malformed.jwt.token";

            boolean isValid = jwtUtils.validateJwtToken(malformedToken);

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner false pour un token expiré")
        void validateJwtToken_ExpiredToken() {
            String expiredToken = Jwts.builder()
                    .setSubject("test@test.com")
                    .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                    .setExpiration(new Date(System.currentTimeMillis() - 5000))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(expiredToken);

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner false pour un token vide")
        void validateJwtToken_EmptyToken() {
            boolean isValid = jwtUtils.validateJwtToken("");

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner false pour un token non supporté")
        void validateJwtToken_UnsupportedToken() {
            // Crée un token sans signature (unsecured JWT)
            String unsupportedToken = Jwts.builder()
                    .setSubject("test@test.com")
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);

            assertThat(isValid).isFalse();
        }
    }
}

