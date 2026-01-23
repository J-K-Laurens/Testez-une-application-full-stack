package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Model Tests")
class UserTest {

    @Test
    @DisplayName("Devrait créer un User avec le builder")
    void builder() {
        LocalDateTime now = LocalDateTime.now();
        
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Devrait créer un User avec le constructeur par défaut et setters")
    void noArgsConstructorAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    @DisplayName("Devrait supporter le chaînage avec @Accessors(chain = true)")
    void chainedSetters() {
        User user = new User()
                .setId(1L)
                .setEmail("chain@test.com")
                .setFirstName("Chain")
                .setLastName("Test");

        assertThat(user.getEmail()).isEqualTo("chain@test.com");
    }

    @Test
    @DisplayName("Devrait avoir equals basé sur l'id")
    void equalsById() {
        User user1 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user2 = User.builder().id(1L).email("b@b.com").firstName("B").lastName("B").password("p").admin(true).build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Ne devrait pas être égal avec des ids différents")
    void notEqualsWithDifferentIds() {
        User user1 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user2 = User.builder().id(2L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("Devrait retourner une représentation toString")
    void toStringTest() {
        User user = User.builder().id(1L).email("test@test.com").firstName("John").lastName("Doe").password("p").admin(false).build();

        String result = user.toString();

        assertThat(result).contains("User");
        assertThat(result).contains("test@test.com");
    }
}

