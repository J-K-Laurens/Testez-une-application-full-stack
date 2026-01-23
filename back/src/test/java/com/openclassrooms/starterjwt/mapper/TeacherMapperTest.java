package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TeacherMapper Tests")
class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);

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
    @DisplayName("ToEntity Tests")
    class ToEntityTests {

        @Test
        @DisplayName("Devrait convertir TeacherDto en Teacher")
        void toEntity_Success() {
            Teacher result = teacherMapper.toEntity(teacherDto);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Devrait retourner null si TeacherDto est null")
        void toEntity_NullDto() {
            Teacher result = teacherMapper.toEntity((TeacherDto) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait convertir une liste de TeacherDto en liste de Teacher")
        void toEntityList_Success() {
            List<Teacher> result = teacherMapper.toEntity(Arrays.asList(teacherDto));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de TeacherDto est null")
        void toEntityList_NullList() {
            List<Teacher> result = teacherMapper.toEntity((List<TeacherDto>) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("ToDto Tests")
    class ToDtoTests {

        @Test
        @DisplayName("Devrait convertir Teacher en TeacherDto")
        void toDto_Success() {
            TeacherDto result = teacherMapper.toDto(teacher);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Devrait retourner null si Teacher est null")
        void toDto_NullTeacher() {
            TeacherDto result = teacherMapper.toDto((Teacher) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait convertir une liste de Teacher en liste de TeacherDto")
        void toDtoList_Success() {
            List<TeacherDto> result = teacherMapper.toDto(Arrays.asList(teacher));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de Teacher est null")
        void toDtoList_NullList() {
            List<TeacherDto> result = teacherMapper.toDto((List<Teacher>) null);

            assertThat(result).isNull();
        }
    }
}

