package com.example.ppay.controller;

import com.example.ppay.config.AccountRepositoryTestConfiguration;
import com.example.ppay.config.UserRepositoryTestConfiguration;
import com.example.ppay.dto.UserDto;
import com.example.ppay.enums.UserType;
import com.example.ppay.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import({UserRepositoryTestConfiguration.class, AccountRepositoryTestConfiguration.class})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    @DisplayName("Should return list of users")
    @Test
    void shouldReturnListOfUses() throws Exception {
        // given
        List<UserDto> expectedUsers = new ArrayList<>();
        expectedUsers.add(new UserDto("1", "Jess", "31813722005", "jess@me.com", "password", String.valueOf(UserType.ENDUSER)));
        expectedUsers.add(new UserDto("2", "Vih", "12887222098", "vih@me.com", "password", String.valueOf(UserType.ENDUSER)));
        // when
        when(userService.findAll()).thenReturn(expectedUsers);
        // then: perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Jess"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName").value("Vih"));
    }

    @DisplayName("Should return a user")
    @Test
    void shouldReturnAUser() throws Exception {
        // given
        var expectedUser = new UserDto("1", "Jess", "31813722005", "jess@me.com", "password", String.valueOf(UserType.ENDUSER));
        // when
        when(userService.findById("1")).thenReturn(expectedUser);
        // then: perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Jess"));
    }

    @DisplayName("Should save and return the saved user")
    @Test
    void shouldSaveUserAndReturnSavedUser() throws Exception {
        // given
        UserDto userDto = new UserDto("1", "Jess", "31813722005", "jess@me.com", "password", String.valueOf(UserType.ENDUSER));
        // when
        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        // then: perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Jess\",\"cpf\":\"31813722005\",\"email\":\"jess@me.com\",\"password\":\"password\",\"userType\":\"ENDUSER\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}