package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teacher Model Tests")
class TeacherTest {

    @Test
    @DisplayName("Devrait créer un Teacher avec le builder")
    void builder() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Devrait créer un Teacher avec le constructeur par défaut et setters")
    void noArgsConstructorAndSetters() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("Jane");
        assertThat(teacher.getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("Devrait supporter le chaînage avec @Accessors(chain = true)")
    void chainedSetters() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Chain")
                .setLastName("Test");

        assertThat(teacher.getFirstName()).isEqualTo("Chain");
    }

    @Test
    @DisplayName("Devrait avoir equals basé sur l'id")
    void equalsById() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("A").lastName("A").build();
        Teacher teacher2 = Teacher.builder().id(1L).firstName("B").lastName("B").build();

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    @DisplayName("Ne devrait pas être égal avec des ids différents")
    void notEqualsWithDifferentIds() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("A").lastName("A").build();
        Teacher teacher2 = Teacher.builder().id(2L).firstName("A").lastName("A").build();

        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    @DisplayName("Devrait retourner une représentation toString")
    void toStringTest() {
        Teacher teacher = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();

        String result = teacher.toString();

        assertThat(result).contains("Teacher");
        assertThat(result).contains("John");
    }
}

