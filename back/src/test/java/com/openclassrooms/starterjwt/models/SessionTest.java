package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session Model Tests")
class SessionTest {

    @Test
    @DisplayName("Devrait créer une Session avec le builder")
    void builder() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        List<User> users = Arrays.asList(
                User.builder().id(1L).email("a@a.com").firstName("A").lastName("A").password("p").admin(false).build()
        );

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(date)
                .description("A relaxing session")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Yoga Session");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("A relaxing session");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Devrait créer une Session avec le constructeur par défaut et setters")
    void noArgsConstructorAndSetters() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDescription("Test description");
        session.setDate(new Date());

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Test Session");
        assertThat(session.getDescription()).isEqualTo("Test description");
    }

    @Test
    @DisplayName("Devrait supporter le chaînage avec @Accessors(chain = true)")
    void chainedSetters() {
        Session session = new Session()
                .setId(1L)
                .setName("Chain Session")
                .setDescription("Chained");

        assertThat(session.getName()).isEqualTo("Chain Session");
    }

    @Test
    @DisplayName("Devrait avoir equals basé sur l'id")
    void equalsById() {
        Session session1 = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        Session session2 = Session.builder().id(1L).name("B").description("B").date(new Date()).build();

        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
    }

    @Test
    @DisplayName("Ne devrait pas être égal avec des ids différents")
    void notEqualsWithDifferentIds() {
        Session session1 = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        Session session2 = Session.builder().id(2L).name("A").description("A").date(new Date()).build();

        assertThat(session1).isNotEqualTo(session2);
    }

    @Test
    @DisplayName("Devrait retourner une représentation toString")
    void toStringTest() {
        Session session = Session.builder().id(1L).name("Yoga").description("Desc").date(new Date()).build();

        String result = session.toString();

        assertThat(result).contains("Session");
        assertThat(result).contains("Yoga");
    }

    @Test
    @DisplayName("Devrait gérer equals avec null et autre type")
    void equalsWithNullAndOtherType() {
        Session session = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        
        assertThat(session.equals(null)).isFalse();
        assertThat(session.equals(new Object())).isFalse();
    }

    @Test
    @DisplayName("Devrait être égal à lui-même")
    void equalsWithSameObject() {
        Session session = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        
        assertThat(session.equals(session)).isTrue();
    }

    @Test
    @DisplayName("Devrait gérer equals avec id null")
    void equalsWithNullId() {
        Session session1 = Session.builder().name("A").description("A").date(new Date()).build();
        Session session2 = Session.builder().name("A").description("A").date(new Date()).build();
        Session session3 = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        
        assertThat(session1).isEqualTo(session2);
        assertThat(session1).isNotEqualTo(session3);
    }

    @Test
    @DisplayName("Builder toString devrait fonctionner")
    void builderToString() {
        String builderString = Session.builder()
                .id(1L)
                .name("Test")
                .toString();
        
        assertThat(builderString).contains("Session.SessionBuilder");
    }

    @Test
    @DisplayName("Devrait utiliser le constructeur avec tous les arguments")
    void allArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = Arrays.asList(new User());
        
        Session session = new Session(1L, "Name", date, "Desc", teacher, users, now, now);
        
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Name");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Desc");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEqualTo(users);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Devrait tester canEqual")
    void canEqual() {
        Session session = new Session();
        assertThat(session.canEqual(new Session())).isTrue();
        assertThat(session.canEqual(new Object())).isFalse();
    }

    @Test
    @DisplayName("Devrait tester equals avec id non null vs id null")
    void equalsIdNotNullVsNull() {
        Session session1 = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        Session session2 = Session.builder().name("A").description("A").date(new Date()).build(); // id null
        
        assertThat(session1).isNotEqualTo(session2);
        assertThat(session2).isNotEqualTo(session1); // Test reverse
    }

    @Test
    @DisplayName("Devrait avoir hashCode cohérent")
    void hashCodeConsistent() {
        Session session1 = Session.builder().id(1L).name("A").description("A").date(new Date()).build();
        Session session2 = Session.builder().name("B").description("B").date(new Date()).build(); // id null
        
        // Null id has consistent hashCode
        int hash = session2.hashCode();
        assertThat(session2.hashCode()).isEqualTo(hash);
    }
}

