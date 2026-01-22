package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionService Tests")
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;
    private Teacher teacher;

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
        session.setUsers(new ArrayList<>());
    }

    @Nested
    @DisplayName("Create Tests")
    class CreateTests {

        @Test
        @DisplayName("Devrait créer une session avec succès")
        void create_Success() {
            when(sessionRepository.save(any(Session.class))).thenReturn(session);

            Session result = sessionService.create(session);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Yoga Session");
            verify(sessionRepository).save(session);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer une session par son ID")
        void delete_Success() {
            doNothing().when(sessionRepository).deleteById(1L);

            sessionService.delete(1L);

            verify(sessionRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Devrait retourner toutes les sessions")
        void findAll_Success() {
            Session session2 = new Session();
            session2.setId(2L);
            session2.setName("Pilates Session");
            List<Session> sessions = Arrays.asList(session, session2);

            when(sessionRepository.findAll()).thenReturn(sessions);

            List<Session> result = sessionService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Yoga Session");
            assertThat(result.get(1).getName()).isEqualTo("Pilates Session");
            verify(sessionRepository).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide si aucune session")
        void findAll_Empty() {
            when(sessionRepository.findAll()).thenReturn(new ArrayList<>());

            List<Session> result = sessionService.findAll();

            assertThat(result).isEmpty();
            verify(sessionRepository).findAll();
        }
    }

    @Nested
    @DisplayName("GetById Tests")
    class GetByIdTests {

        @Test
        @DisplayName("Devrait retourner une session par son ID")
        void getById_Success() {
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

            Session result = sessionService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(sessionRepository).findById(1L);
        }

        @Test
        @DisplayName("Devrait retourner null si la session n'existe pas")
        void getById_NotFound() {
            when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

            Session result = sessionService.getById(999L);

            assertThat(result).isNull();
            verify(sessionRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Devrait mettre à jour une session")
        void update_Success() {
            Session updatedSession = new Session();
            updatedSession.setName("Updated Yoga Session");
            updatedSession.setDescription("Updated description");

            when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
                Session s = invocation.getArgument(0);
                s.setId(1L);
                return s;
            });

            Session result = sessionService.update(1L, updatedSession);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Updated Yoga Session");
            verify(sessionRepository).save(updatedSession);
        }
    }

    @Nested
    @DisplayName("Participate Tests")
    class ParticipateTests {

        @Test
        @DisplayName("Devrait ajouter un utilisateur à une session")
        void participate_Success() {
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(sessionRepository.save(any(Session.class))).thenReturn(session);

            sessionService.participate(1L, 1L);

            assertThat(session.getUsers()).contains(user);
            verify(sessionRepository).save(session);
        }

        @Test
        @DisplayName("Devrait lancer NotFoundException si la session n'existe pas")
        void participate_SessionNotFound() {
            when(sessionRepository.findById(999L)).thenReturn(Optional.empty());
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> sessionService.participate(999L, 1L))
                    .isInstanceOf(NotFoundException.class);

            verify(sessionRepository, never()).save(any(Session.class));
        }

        @Test
        @DisplayName("Devrait lancer NotFoundException si l'utilisateur n'existe pas")
        void participate_UserNotFound() {
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sessionService.participate(1L, 999L))
                    .isInstanceOf(NotFoundException.class);

            verify(sessionRepository, never()).save(any(Session.class));
        }

        @Test
        @DisplayName("Devrait lancer BadRequestException si l'utilisateur participe déjà")
        void participate_AlreadyParticipating() {
            session.getUsers().add(user);
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> sessionService.participate(1L, 1L))
                    .isInstanceOf(BadRequestException.class);

            verify(sessionRepository, never()).save(any(Session.class));
        }
    }

    @Nested
    @DisplayName("NoLongerParticipate Tests")
    class NoLongerParticipateTests {

        @Test
        @DisplayName("Devrait retirer un utilisateur d'une session")
        void noLongerParticipate_Success() {
            session.getUsers().add(user);
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
            when(sessionRepository.save(any(Session.class))).thenReturn(session);

            sessionService.noLongerParticipate(1L, 1L);

            assertThat(session.getUsers()).doesNotContain(user);
            verify(sessionRepository).save(session);
        }

        @Test
        @DisplayName("Devrait lancer NotFoundException si la session n'existe pas")
        void noLongerParticipate_SessionNotFound() {
            when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sessionService.noLongerParticipate(999L, 1L))
                    .isInstanceOf(NotFoundException.class);

            verify(sessionRepository, never()).save(any(Session.class));
        }

        @Test
        @DisplayName("Devrait lancer BadRequestException si l'utilisateur ne participe pas")
        void noLongerParticipate_NotParticipating() {
            when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

            assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 1L))
                    .isInstanceOf(BadRequestException.class);

            verify(sessionRepository, never()).save(any(Session.class));
        }
    }
}

