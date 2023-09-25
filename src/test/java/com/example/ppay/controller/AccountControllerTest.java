package com.example.ppay.controller;

import com.example.ppay.config.AccountRepositoryTestConfiguration;
import com.example.ppay.config.UserRepositoryTestConfiguration;
import com.example.ppay.dto.AccountResponseDto;
import com.example.ppay.service.AccountService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@Import({UserRepositoryTestConfiguration.class, AccountRepositoryTestConfiguration.class})
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AccountService accountService;

    @DisplayName("Should return list of accounts")
    @Test
    void shouldReturnListOfAccounts() throws Exception {
        // given
        List<AccountResponseDto> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(new AccountResponseDto(12345, new BigDecimal("100"), "Jess"));
        expectedAccounts.add(new AccountResponseDto(67890, new BigDecimal("100"), "Vih"));
        // when
        when(accountService.findAll()).thenReturn(expectedAccounts);
        // then: perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value(12345))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value("100"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Jess"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].number").value(67890))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value("100"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName").value("Vih"));
    }

}