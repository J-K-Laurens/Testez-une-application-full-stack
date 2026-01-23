package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SignupRequest Tests")
class SignupRequestTest {

    @Test
    @DisplayName("Devrait créer un SignupRequest et accéder aux propriétés")
    void gettersAndSetters() {
        SignupRequest request = new SignupRequest();
        
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");
        
        assertThat(request.getEmail()).isEqualTo("test@example.com");
        assertThat(request.getFirstName()).isEqualTo("John");
        assertThat(request.getLastName()).isEqualTo("Doe");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Devrait avoir equals et hashCode fonctionnels")
    void equalsAndHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Devrait retourner une représentation toString")
    void toStringTest() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        
        String result = request.toString();
        
        assertThat(result).contains("test@example.com");
        assertThat(result).contains("John");
    }

    @Test
    @DisplayName("Ne devrait pas être égal à un objet différent")
    void notEquals() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test1@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test2@example.com");

        assertThat(request1).isNotEqualTo(request2);
    }
}

