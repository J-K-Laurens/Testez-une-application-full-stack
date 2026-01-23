package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MessageResponse Tests")
class MessageResponseTest {

    @Test
    @DisplayName("Devrait créer un MessageResponse avec le constructeur")
    void constructor() {
        MessageResponse response = new MessageResponse("Success message");

        assertThat(response.getMessage()).isEqualTo("Success message");
    }

    @Test
    @DisplayName("Devrait modifier le message avec le setter")
    void setter() {
        MessageResponse response = new MessageResponse("Initial");

        response.setMessage("Updated message");

        assertThat(response.getMessage()).isEqualTo("Updated message");
    }
}

