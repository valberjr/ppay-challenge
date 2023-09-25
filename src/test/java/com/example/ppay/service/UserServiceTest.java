package com.example.ppay.service;

import com.example.ppay.dto.UserDto;
import com.example.ppay.dto.mapper.UserMapper;
import com.example.ppay.enums.UserType;
import com.example.ppay.exception.OperationNotAllowedException;
import com.example.ppay.model.User;
import com.example.ppay.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    UserMapper mapper;

    @InjectMocks
    private UserService service;

    @BeforeEach
    public void setup() {

    }

    @DisplayName("Should return an empty list when no users exist")
    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        // given
        given(repository.findAll()).willReturn(Collections.emptyList());

        // when
        var users = service.findAll();

        // then
        assertThat(users).isEmpty();
    }

    @DisplayName("Should throw EntityNotFoundException when user with given id does not exist")
    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserWithGivenIdDoesNotExist() {
        // given
        String userId = "3263ee01-4669-49ef-8bae-b830cf7918f5";

        // when
        when(repository.findById(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> service.findById(userId));
    }

    @DisplayName("Should save a user and return the saved user")
    @Test
    void shouldSaveUserAndReturnSavedUser() {
        // given
        UserDto userDto = new UserDto("3263ee01", "Jess", "31813722005", "jess@me.com", "password", String.valueOf(UserType.ENDUSER));
        User user = User.builder()
                .id("3263ee01")
                .fullName("Jess")
                .cpf("31813722005")
                .email("jess@me.com")
                .password("password")
                .userType(UserType.ENDUSER)
                .build();
        // when
        when(repository.save(any(User.class))).thenReturn(user);
        when(mapper.toEntity(userDto)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(userDto);

        UserDto result = service.save(userDto);

        // then
        assertEquals(userDto, result);
        verify(repository, times(1)).save(any(User.class));
        verify(mapper, times(1)).toEntity(userDto);
        verify(mapper, times(1)).toDto(user);
    }

    @DisplayName("Test for checking if user type is merchant")
    @Test
    void shouldThrowOperationNotAllowedExceptionWhenUserTypeIsMerchant() {
        // given
        UserType userType = UserType.MERCHANT;

        // when and then
        assertThrows(OperationNotAllowedException.class, () -> service.isMerchant(userType));
    }

    @DisplayName("Test for checking if user type is not merchant")
    @Test
    void shouldNotThrowOperationNotAllowedExceptionWhenUserTypeIsNotMerchant() {
        // given
        UserType userType = UserType.ENDUSER;

        // when and then
        assertDoesNotThrow(() -> service.isMerchant(userType));
    }

}