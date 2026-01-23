package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SessionMapper Integration Tests")
class SessionMapperIntegrationTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        teacher = teacherRepository.save(new Teacher().setFirstName("John").setLastName("Doe"));
        user = userRepository.save(new User()
                .setEmail("user@test.com")
                .setFirstName("Test")
                .setLastName("User")
                .setPassword("pass")
                .setAdmin(false));
    }

    @Test
    @DisplayName("Devrait convertir un DTO en Entity avec vraie DB")
    void toEntityWithRealData() {
        SessionDto dto = new SessionDto();
        dto.setName("Integration Session");
        dto.setDescription("Test integration");
        dto.setDate(new Date());
        dto.setTeacher_id(teacher.getId());
        dto.setUsers(Arrays.asList(user.getId()));

        Session session = sessionMapper.toEntity(dto);

        assertThat(session.getName()).isEqualTo("Integration Session");
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(session.getUsers()).hasSize(1);
    }

    @Test
    @DisplayName("Devrait convertir une Entity en DTO")
    void toDtoWithRealData() {
        Session session = new Session()
                .setId(1L)
                .setName("Session Test")
                .setDescription("Desc")
                .setDate(new Date())
                .setTeacher(teacher)
                .setUsers(Arrays.asList(user));

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto.getName()).isEqualTo("Session Test");
        assertThat(dto.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(dto.getUsers()).contains(user.getId());
    }
}

