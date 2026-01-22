package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionController Tests")
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>());

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("A relaxing yoga session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(new ArrayList<>());
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Devrait retourner une session par son ID")
        void findById_Success() {
            when(sessionService.getById(1L)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> response = sessionController.findById("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(sessionDto);
            verify(sessionService).getById(1L);
        }

        @Test
        @DisplayName("Devrait retourner 404 si la session n'existe pas")
        void findById_NotFound() {
            when(sessionService.getById(999L)).thenReturn(null);

            ResponseEntity<?> response = sessionController.findById("999");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(sessionService).getById(999L);
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void findById_BadRequest() {
            ResponseEntity<?> response = sessionController.findById("invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).getById(anyLong());
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Devrait retourner toutes les sessions")
        void findAll_Success() {
            List<Session> sessions = Arrays.asList(session);
            List<SessionDto> sessionDtos = Arrays.asList(sessionDto);

            when(sessionService.findAll()).thenReturn(sessions);
            when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

            ResponseEntity<?> response = sessionController.findAll();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(sessionDtos);
            verify(sessionService).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide si aucune session")
        void findAll_Empty() {
            when(sessionService.findAll()).thenReturn(new ArrayList<>());
            when(sessionMapper.toDto(anyList())).thenReturn(new ArrayList<>());

            ResponseEntity<?> response = sessionController.findAll();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(new ArrayList<>());
        }
    }

    @Nested
    @DisplayName("Create Tests")
    class CreateTests {

        @Test
        @DisplayName("Devrait créer une nouvelle session")
        void create_Success() {
            when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
            when(sessionService.create(session)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> response = sessionController.create(sessionDto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(sessionDto);
            verify(sessionService).create(session);
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Devrait mettre à jour une session existante")
        void update_Success() {
            when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
            when(sessionService.update(1L, session)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> response = sessionController.update("1", sessionDto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(sessionDto);
            verify(sessionService).update(1L, session);
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void update_BadRequest() {
            ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).update(anyLong(), any(Session.class));
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer une session existante")
        void delete_Success() {
            when(sessionService.getById(1L)).thenReturn(session);
            doNothing().when(sessionService).delete(1L);

            ResponseEntity<?> response = sessionController.save("1");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(sessionService).delete(1L);
        }

        @Test
        @DisplayName("Devrait retourner 404 si la session n'existe pas")
        void delete_NotFound() {
            when(sessionService.getById(999L)).thenReturn(null);

            ResponseEntity<?> response = sessionController.save("999");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            verify(sessionService, never()).delete(anyLong());
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID est invalide")
        void delete_BadRequest() {
            ResponseEntity<?> response = sessionController.save("invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).delete(anyLong());
        }
    }

    @Nested
    @DisplayName("Participate Tests")
    class ParticipateTests {

        @Test
        @DisplayName("Devrait permettre la participation à une session")
        void participate_Success() {
            doNothing().when(sessionService).participate(1L, 2L);

            ResponseEntity<?> response = sessionController.participate("1", "2");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(sessionService).participate(1L, 2L);
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID session est invalide")
        void participate_BadRequest_InvalidSessionId() {
            ResponseEntity<?> response = sessionController.participate("invalid", "2");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).participate(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID utilisateur est invalide")
        void participate_BadRequest_InvalidUserId() {
            ResponseEntity<?> response = sessionController.participate("1", "invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).participate(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("NoLongerParticipate Tests")
    class NoLongerParticipateTests {

        @Test
        @DisplayName("Devrait permettre de se désinscrire d'une session")
        void noLongerParticipate_Success() {
            doNothing().when(sessionService).noLongerParticipate(1L, 2L);

            ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(sessionService).noLongerParticipate(1L, 2L);
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID session est invalide")
        void noLongerParticipate_BadRequest_InvalidSessionId() {
            ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "2");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Devrait retourner 400 si l'ID utilisateur est invalide")
        void noLongerParticipate_BadRequest_InvalidUserId() {
            ResponseEntity<?> response = sessionController.noLongerParticipate("1", "invalid");

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
        }
    }
}

