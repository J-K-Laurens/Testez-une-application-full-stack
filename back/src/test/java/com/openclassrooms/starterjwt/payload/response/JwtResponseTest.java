package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtResponse Tests")
class JwtResponseTest {

    @Test
    @DisplayName("Devrait créer un JwtResponse avec le constructeur")
    void constructor() {
        JwtResponse response = new JwtResponse("token123", 1L, "user@test.com", "John", "Doe", true);

        assertThat(response.getToken()).isEqualTo("token123");
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("user@test.com");
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getAdmin()).isTrue();
        assertThat(response.getType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Devrait modifier les valeurs avec les setters")
    void setters() {
        JwtResponse response = new JwtResponse("token", 1L, "user", "First", "Last", false);

        response.setToken("newToken");
        response.setId(2L);
        response.setUsername("newUser");
        response.setFirstName("NewFirst");
        response.setLastName("NewLast");
        response.setAdmin(true);
        response.setType("Custom");

        assertThat(response.getToken()).isEqualTo("newToken");
        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getUsername()).isEqualTo("newUser");
        assertThat(response.getFirstName()).isEqualTo("NewFirst");
        assertThat(response.getLastName()).isEqualTo("NewLast");
        assertThat(response.getAdmin()).isTrue();
        assertThat(response.getType()).isEqualTo("Custom");
    }
}

