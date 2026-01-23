package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SessionService Integration Tests")
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        teacher = teacherRepository.save(new Teacher().setFirstName("John").setLastName("Doe"));
        user = userRepository.save(new User().setEmail("user@test.com").setFirstName("Test").setLastName("User").setPassword("pass").setAdmin(false));
    }

    @Test
    @DisplayName("Devrait créer une session")
    void createSession() {
        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("Relaxing session");
        session.setDate(new Date());
        session.setTeacher(teacher);

        Session created = sessionService.create(session);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Yoga Session");
    }

    @Test
    @DisplayName("Devrait récupérer toutes les sessions")
    void findAllSessions() {
        sessionRepository.save(new Session().setName("Session 1").setDescription("Desc").setDate(new Date()).setTeacher(teacher));
        sessionRepository.save(new Session().setName("Session 2").setDescription("Desc").setDate(new Date()).setTeacher(teacher));

        List<Session> sessions = sessionService.findAll();

        assertThat(sessions).hasSize(2);
    }

    @Test
    @DisplayName("Devrait récupérer une session par ID")
    void getById() {
        Session saved = sessionRepository.save(new Session().setName("Test").setDescription("Desc").setDate(new Date()).setTeacher(teacher));

        Session found = sessionService.getById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Devrait supprimer une session")
    void deleteSession() {
        Session saved = sessionRepository.save(new Session().setName("ToDelete").setDescription("Desc").setDate(new Date()).setTeacher(teacher));

        sessionService.delete(saved.getId());

        assertThat(sessionRepository.findById(saved.getId())).isEmpty();
    }
}

