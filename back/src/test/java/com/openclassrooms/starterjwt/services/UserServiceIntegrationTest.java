package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService Integration Tests")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Devrait récupérer un user par ID")
    void findById() {
        User saved = userRepository.save(new User()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password")
                .setAdmin(false));

        User found = userService.findById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Devrait retourner null pour un ID inexistant")
    void findByIdNotFound() {
        User found = userService.findById(99999L);

        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Devrait supprimer un user")
    void deleteUser() {
        User saved = userRepository.save(new User()
                .setEmail("delete@example.com")
                .setFirstName("Delete")
                .setLastName("Me")
                .setPassword("password")
                .setAdmin(false));

        userService.delete(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}

