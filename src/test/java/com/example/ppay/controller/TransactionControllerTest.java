package com.example.ppay.controller;

import com.example.ppay.config.AccountRepositoryTestConfiguration;
import com.example.ppay.config.UserRepositoryTestConfiguration;
import com.example.ppay.dto.TransactionDto;
import com.example.ppay.dto.mapper.TransactionDtoResponse;
import com.example.ppay.service.TransactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
@Import({UserRepositoryTestConfiguration.class, AccountRepositoryTestConfiguration.class})
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TransactionService transactionService;

    @DisplayName("Should return a successful transaction response")
    @Test
    void shouldReturnSuccessfulTransactionResponse() throws Exception {
        // given
        TransactionDtoResponse response = new TransactionDtoResponse("Transfer successfully completed");
        // when
        when(transactionService.sendMoney(any(TransactionDto.class))).thenReturn(response);
        // then: perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderId\":\"1\",\"receiverId\":\"2\",\"amount\":100}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Transfer successfully completed"));
    }

}