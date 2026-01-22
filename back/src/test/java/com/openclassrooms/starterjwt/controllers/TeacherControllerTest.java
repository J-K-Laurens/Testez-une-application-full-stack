package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherController Tests")
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Devrait retourner un teacher par son ID")
        void findById_Success() {
            when(teacherService.findById(1L)).thenReturn(teacher);
            when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

            ResponseEntity<?> response = teacherController.findById("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(teacherDto);
            verify(teacherService).findById(1L);
            verify(teacherMapper).toDto(teacher);
        }

        @Test
        @DisplayName("Devrait retourner 404 si le teacher n'existe pas")
        void findById_NotFound() {
            when(teacherService.findById(999L)).thenReturn(null);

            ResponseEntity<?> response = teacherController.findById("999");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(teacherService).findById(999L);
            verify(teacherMapper, never()).toDto(any(Teacher.class));
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void findById_BadRequest() {
            ResponseEntity<?> response = teacherController.findById("invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(teacherService, never()).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Devrait retourner tous les teachers")
        void findAll_Success() {
            Teacher teacher2 = new Teacher();
            teacher2.setId(2L);
            teacher2.setFirstName("Jane");
            teacher2.setLastName("Smith");

            TeacherDto teacherDto2 = new TeacherDto();
            teacherDto2.setId(2L);
            teacherDto2.setFirstName("Jane");
            teacherDto2.setLastName("Smith");

            List<Teacher> teachers = Arrays.asList(teacher, teacher2);
            List<TeacherDto> teacherDtos = Arrays.asList(teacherDto, teacherDto2);

            when(teacherService.findAll()).thenReturn(teachers);
            when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

            ResponseEntity<?> response = teacherController.findAll();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(teacherDtos);
            verify(teacherService).findAll();
            verify(teacherMapper).toDto(teachers);
        }

        @Test
        @DisplayName("Devrait retourner une liste vide si aucun teacher")
        void findAll_Empty() {
            when(teacherService.findAll()).thenReturn(new ArrayList<>());
            when(teacherMapper.toDto(anyList())).thenReturn(new ArrayList<>());

            ResponseEntity<?> response = teacherController.findAll();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(new ArrayList<>());
            verify(teacherService).findAll();
        }
    }
}

