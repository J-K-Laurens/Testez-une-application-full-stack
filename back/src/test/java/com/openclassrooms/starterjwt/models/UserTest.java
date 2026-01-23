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

    @Test
    @DisplayName("Devrait gérer equals avec null et autre type")
    void equalsWithNullAndOtherType() {
        User user = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        
        assertThat(user.equals(null)).isFalse();
        assertThat(user.equals(new Object())).isFalse();
        assertThat(user.equals("string")).isFalse();
    }

    @Test
    @DisplayName("Devrait être égal à lui-même")
    void equalsWithSameObject() {
        User user = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        
        assertThat(user.equals(user)).isTrue();
    }

    @Test
    @DisplayName("Devrait gérer equals avec id null")
    void equalsWithNullId() {
        User user1 = User.builder().email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user2 = User.builder().email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user3 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        
        // Two users with null id
        assertThat(user1).isEqualTo(user2);
        // User with null id vs user with id
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    @DisplayName("Builder toString devrait fonctionner")
    void builderToString() {
        String builderString = User.builder()
                .id(1L)
                .email("test@test.com")
                .toString();
        
        assertThat(builderString).contains("User.UserBuilder");
    }

    @Test
    @DisplayName("Devrait utiliser le constructeur avec arguments requis")
    void requiredArgsConstructor() {
        User user = new User("email@test.com", "Doe", "John", "password", true);
        
        assertThat(user.getEmail()).isEqualTo("email@test.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("Devrait utiliser le constructeur avec tous les arguments")
    void allArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "email@test.com", "Doe", "John", "password", true, now, now);
        
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("email@test.com");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Devrait tester canEqual")
    void canEqual() {
        User user = new User();
        assertThat(user.canEqual(new User())).isTrue();
        assertThat(user.canEqual(new Object())).isFalse();
    }

    @Test
    @DisplayName("Devrait tester equals avec id non null vs id null")
    void equalsIdNotNullVsNull() {
        User user1 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user2 = User.builder().email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build(); // id null
        
        assertThat(user1).isNotEqualTo(user2);
        assertThat(user2).isNotEqualTo(user1); // Test reverse
    }

    @Test
    @DisplayName("Devrait avoir hashCode cohérent")
    void hashCodeConsistent() {
        User user1 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build();
        User user2 = User.builder().id(1L).email("b@b.com").firstName("B").lastName("B").password("q").admin(true).build();
        User user3 = User.builder().email("c@c.com").firstName("C").lastName("C").password("r").admin(false).build(); // id null
        
        // Same id = same hashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        
        // Null id has consistent hashCode
        int hash = user3.hashCode();
        assertThat(user3.hashCode()).isEqualTo(hash);
    }

    @Test
    @DisplayName("Builder avec tous les champs null pour toString")
    void builderToStringWithNullFields() {
        String builderString = User.builder().toString();
        assertThat(builderString).contains("UserBuilder");
    }

    @Test
    @DisplayName("Builder avec tous les champs définis pour toString")
    void builderToStringWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        String builderString = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .toString();
        
        assertThat(builderString).contains("id=1");
        assertThat(builderString).contains("email=test@test.com");
        assertThat(builderString).contains("firstName=John");
        assertThat(builderString).contains("lastName=Doe");
    }

    @Test
    @DisplayName("Devrait tester setters avec valeurs null")
    void settersWithNull() {
        User user = new User();
        user.setId(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        
        assertThat(user.getId()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }
}

