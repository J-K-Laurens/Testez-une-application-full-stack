package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionMapper Tests")
class SessionMapperTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setFirstName("Test");
        user.setLastName("User");

        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("A relaxing yoga session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L));
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("ToEntity Tests")
    class ToEntityTests {

        @Test
        @DisplayName("Devrait convertir SessionDto en Session")
        void toEntity_Success() {
            when(teacherService.findById(1L)).thenReturn(teacher);
            when(userService.findById(1L)).thenReturn(user);

            Session result = sessionMapper.toEntity(sessionDto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Yoga Session");
            assertThat(result.getDescription()).isEqualTo("A relaxing yoga session");
            assertThat(result.getTeacher()).isEqualTo(teacher);
            assertThat(result.getUsers()).hasSize(1);
        }

        @Test
        @DisplayName("Devrait retourner null si SessionDto est null")
        void toEntity_NullDto() {
            Session result = sessionMapper.toEntity((SessionDto) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait gérer teacher_id null")
        void toEntity_NullTeacherId() {
            sessionDto.setTeacher_id(null);

            Session result = sessionMapper.toEntity(sessionDto);

            assertThat(result).isNotNull();
            assertThat(result.getTeacher()).isNull();
        }

        @Test
        @DisplayName("Devrait gérer users null")
        void toEntity_NullUsers() {
            sessionDto.setUsers(null);

            Session result = sessionMapper.toEntity(sessionDto);

            assertThat(result).isNotNull();
            assertThat(result.getUsers()).isEmpty();
        }

        @Test
        @DisplayName("Devrait convertir une liste de SessionDto en liste de Session")
        void toEntityList_Success() {
            when(teacherService.findById(1L)).thenReturn(teacher);
            when(userService.findById(1L)).thenReturn(user);

            List<Session> result = sessionMapper.toEntity(Arrays.asList(sessionDto));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Yoga Session");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de SessionDto est null")
        void toEntityList_NullList() {
            List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("ToDto Tests")
    class ToDtoTests {

        @Test
        @DisplayName("Devrait convertir Session en SessionDto")
        void toDto_Success() {
            SessionDto result = sessionMapper.toDto(session);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Yoga Session");
            assertThat(result.getDescription()).isEqualTo("A relaxing yoga session");
            assertThat(result.getTeacher_id()).isEqualTo(1L);
            assertThat(result.getUsers()).hasSize(1);
            assertThat(result.getUsers().get(0)).isEqualTo(1L);
        }

        @Test
        @DisplayName("Devrait retourner null si Session est null")
        void toDto_NullSession() {
            SessionDto result = sessionMapper.toDto((Session) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait gérer teacher null")
        void toDto_NullTeacher() {
            session.setTeacher(null);

            SessionDto result = sessionMapper.toDto(session);

            assertThat(result).isNotNull();
            assertThat(result.getTeacher_id()).isNull();
        }

        @Test
        @DisplayName("Devrait gérer users null")
        void toDto_NullUsers() {
            session.setUsers(null);

            SessionDto result = sessionMapper.toDto(session);

            assertThat(result).isNotNull();
            assertThat(result.getUsers()).isEmpty();
        }

        @Test
        @DisplayName("Devrait convertir une liste de Session en liste de SessionDto")
        void toDtoList_Success() {
            List<SessionDto> result = sessionMapper.toDto(Arrays.asList(session));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Yoga Session");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de Session est null")
        void toDtoList_NullList() {
            List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

            assertThat(result).isNull();
        }
    }
}

