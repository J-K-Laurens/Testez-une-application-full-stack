package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherService Tests")
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Devrait retourner tous les enseignants")
        void findAll_Success() {
            Teacher teacher2 = new Teacher();
            teacher2.setId(2L);
            teacher2.setFirstName("Jane");
            teacher2.setLastName("Smith");
            List<Teacher> teachers = Arrays.asList(teacher, teacher2);

            when(teacherRepository.findAll()).thenReturn(teachers);

            List<Teacher> result = teacherService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getFirstName()).isEqualTo("John");
            assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
            verify(teacherRepository).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide si aucun enseignant")
        void findAll_Empty() {
            when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

            List<Teacher> result = teacherService.findAll();

            assertThat(result).isEmpty();
            verify(teacherRepository).findAll();
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Devrait retourner un enseignant par son ID")
        void findById_Success() {
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

            Teacher result = teacherService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            verify(teacherRepository).findById(1L);
        }

        @Test
        @DisplayName("Devrait retourner null si l'enseignant n'existe pas")
        void findById_NotFound() {
            when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

            Teacher result = teacherService.findById(999L);

            assertThat(result).isNull();
            verify(teacherRepository).findById(999L);
        }
    }
}

