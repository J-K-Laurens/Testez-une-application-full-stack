package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SpringBootSecurityJwtApplication Tests")
class SpringBootSecurityJwtApplicationTest {

    @Test
    @DisplayName("Devrait charger le contexte Spring")
    void contextLoads() {
        // Le contexte se charge automatiquement avec @SpringBootTest
    }

    @Test
    @DisplayName("Devrait exécuter la méthode main sans erreur")
    void mainMethodRuns() {
        assertThatNoException().isThrownBy(() -> 
            SpringBootSecurityJwtApplication.main(new String[]{})
        );
    }
}

