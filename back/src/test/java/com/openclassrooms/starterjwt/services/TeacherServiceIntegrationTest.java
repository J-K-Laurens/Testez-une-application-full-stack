package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TeacherService Integration Tests")
class TeacherServiceIntegrationTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
    }

    @Test
    @DisplayName("Devrait récupérer tous les teachers")
    void findAllTeachers() {
        teacherRepository.save(new Teacher().setFirstName("John").setLastName("Doe"));
        teacherRepository.save(new Teacher().setFirstName("Jane").setLastName("Smith"));

        List<Teacher> teachers = teacherService.findAll();

        assertThat(teachers).hasSize(2);
    }

    @Test
    @DisplayName("Devrait récupérer un teacher par ID")
    void findById() {
        Teacher saved = teacherRepository.save(new Teacher().setFirstName("John").setLastName("Doe"));

        Teacher found = teacherService.findById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Devrait retourner null pour un ID inexistant")
    void findByIdNotFound() {
        Teacher found = teacherService.findById(99999L);

        assertThat(found).isNull();
    }
}

