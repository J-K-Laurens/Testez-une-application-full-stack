package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDetailsImpl Tests")
class UserDetailsImplTest {

    @Test
    @DisplayName("Devrait créer un UserDetailsImpl avec le builder")
    void builder() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getFirstName()).isEqualTo("John");
        assertThat(userDetails.getLastName()).isEqualTo("Doe");
        assertThat(userDetails.getAdmin()).isTrue();
        assertThat(userDetails.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Devrait retourner des authorities vides")
    void getAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .build();

        assertThat(userDetails.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("Devrait retourner true pour isAccountNonExpired")
    void isAccountNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Devrait retourner true pour isAccountNonLocked")
    void isAccountNonLocked() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    @DisplayName("Devrait retourner true pour isCredentialsNonExpired")
    void isCredentialsNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Devrait retourner true pour isEnabled")
    void isEnabled() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Devrait être égal à lui-même")
    void equalsToSelf() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).username("test").build();

        assertThat(userDetails.equals(userDetails)).isTrue();
    }

    @Test
    @DisplayName("Devrait être égal avec le même id")
    void equalsSameId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).username("user1").build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).username("user2").build();

        assertThat(user1.equals(user2)).isTrue();
    }

    @Test
    @DisplayName("Ne devrait pas être égal avec des ids différents")
    void notEqualsDifferentId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).username("user").build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(2L).username("user").build();

        assertThat(user1.equals(user2)).isFalse();
    }

    @Test
    @DisplayName("Ne devrait pas être égal à null")
    void notEqualsNull() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Ne devrait pas être égal à un objet d'un autre type")
    void notEqualsDifferentType() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertThat(userDetails.equals("string")).isFalse();
    }
}

