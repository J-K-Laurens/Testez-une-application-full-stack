package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("ToEntity Tests")
    class ToEntityTests {

        @Test
        @DisplayName("Devrait convertir UserDto en User")
        void toEntity_Success() {
            User result = userMapper.toEntity(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("test@test.com");
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
            assertThat(result.isAdmin()).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner null si UserDto est null")
        void toEntity_NullDto() {
            User result = userMapper.toEntity((UserDto) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait convertir une liste de UserDto en liste de User")
        void toEntityList_Success() {
            List<User> result = userMapper.toEntity(Arrays.asList(userDto));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo("test@test.com");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de UserDto est null")
        void toEntityList_NullList() {
            List<User> result = userMapper.toEntity((List<UserDto>) null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("ToDto Tests")
    class ToDtoTests {

        @Test
        @DisplayName("Devrait convertir User en UserDto")
        void toDto_Success() {
            UserDto result = userMapper.toDto(user);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("test@test.com");
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
            assertThat(result.isAdmin()).isFalse();
        }

        @Test
        @DisplayName("Devrait retourner null si User est null")
        void toDto_NullUser() {
            UserDto result = userMapper.toDto((User) null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Devrait convertir une liste de User en liste de UserDto")
        void toDtoList_Success() {
            List<UserDto> result = userMapper.toDto(Arrays.asList(user));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo("test@test.com");
        }

        @Test
        @DisplayName("Devrait retourner null si la liste de User est null")
        void toDtoList_NullList() {
            List<UserDto> result = userMapper.toDto((List<User>) null);

            assertThat(result).isNull();
        }
    }
}

