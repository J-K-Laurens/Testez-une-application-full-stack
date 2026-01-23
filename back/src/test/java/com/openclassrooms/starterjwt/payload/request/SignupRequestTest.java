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

    @Test
    @DisplayName("Devrait gérer equals avec null et autre type")
    void equalsWithNullAndOtherType() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        
        assertThat(request.equals(null)).isFalse();
        assertThat(request.equals(new Object())).isFalse();
        assertThat(request.equals("string")).isFalse();
    }

    @Test
    @DisplayName("Devrait être égal à lui-même")
    void equalsWithSameObject() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        
        assertThat(request.equals(request)).isTrue();
    }

    @Test
    @DisplayName("Devrait gérer equals avec champs null")
    void equalsWithNullFields() {
        SignupRequest request1 = new SignupRequest();
        SignupRequest request2 = new SignupRequest();
        
        // Deux objets avec tous les champs null
        assertThat(request1).isEqualTo(request2);
        
        // Un seul champ différent
        request1.setEmail("test@example.com");
        assertThat(request1).isNotEqualTo(request2);
        
        // Email égal, firstName différent
        request2.setEmail("test@example.com");
        request1.setFirstName("John");
        assertThat(request1).isNotEqualTo(request2);
        
        // Email et firstName égaux, lastName différent
        request2.setFirstName("John");
        request1.setLastName("Doe");
        assertThat(request1).isNotEqualTo(request2);
        
        // Tous égaux sauf password
        request2.setLastName("Doe");
        request1.setPassword("pass1");
        assertThat(request1).isNotEqualTo(request2);
        
        // Tous égaux
        request2.setPassword("pass1");
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    @DisplayName("Devrait tester canEqual")
    void canEqual() {
        SignupRequest request = new SignupRequest();
        assertThat(request.canEqual(new SignupRequest())).isTrue();
        assertThat(request.canEqual(new Object())).isFalse();
    }

    @Test
    @DisplayName("Devrait avoir hashCode différent pour objets différents")
    void hashCodeDifferent() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test1@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("pass1");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test2@example.com");
        request2.setFirstName("Jane");
        request2.setLastName("Smith");
        request2.setPassword("pass2");

        assertThat(request1.hashCode()).isNotEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Devrait avoir hashCode cohérent avec champs null")
    void hashCodeWithNullFields() {
        SignupRequest request1 = new SignupRequest();
        SignupRequest request2 = new SignupRequest();
        
        // Deux objets vides ont le même hashCode
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }
}

