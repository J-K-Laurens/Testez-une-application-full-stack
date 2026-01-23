package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginRequest Tests")
class LoginRequestTest {

    @Test
    @DisplayName("Devrait créer un LoginRequest et accéder aux propriétés")
    void gettersAndSetters() {
        LoginRequest request = new LoginRequest();
        
        request.setEmail("test@example.com");
        request.setPassword("password123");
        
        assertThat(request.getEmail()).isEqualTo("test@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Devrait permettre de modifier les valeurs")
    void modifyValues() {
        LoginRequest request = new LoginRequest();
        request.setEmail("old@example.com");
        request.setPassword("oldpass");
        
        request.setEmail("new@example.com");
        request.setPassword("newpass");
        
        assertThat(request.getEmail()).isEqualTo("new@example.com");
        assertThat(request.getPassword()).isEqualTo("newpass");
    }
}

