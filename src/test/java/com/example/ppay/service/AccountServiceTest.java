package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.exception.BalanceException;
import com.example.ppay.model.Account;
import com.example.ppay.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository repository;
    @Mock
    AccountMapper mapper;
    @InjectMocks
    AccountService service;

    AccountDto accountDto;
    Account account;

    @BeforeEach
    public void setup() {
        accountDto = new AccountDto("1253ff02", 1, BigDecimal.ZERO, null);
        account = Account.builder()
                .id("1253ff02")
                .number(1)
                .balance(new BigDecimal("100"))
                .build();
    }

    @DisplayName("Should return an empty list when no accounts exist")
    @Test
    void shouldReturnEmptyListWhenNoAccountsExist() {
        // given
        given(repository.findAll()).willReturn(Collections.emptyList());
        // then
        assertThat(service.findAll()).isEmpty();
    }

    @DisplayName("Should throw EntityNotFoundException when user with given id does not exist")
    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserWithGivenIdDoesNotExist() {
        // given
        String userId = "3263ee01-4669-49ef-8bae-b830cf7918f5";
        // when
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());
        // then
        assertThrows(EntityNotFoundException.class, () -> service.findByUserId(userId));
    }

    @DisplayName("Should update an account balance subtracting the amount")
    @Test
    void shouldUpdateAccountBalanceSubtractingAmount() {
        // given
        boolean isSender = true;
        // when
        when(mapper.toEntity(accountDto)).thenReturn(account);
        when(repository.save(any())).thenReturn(account);
        service.updateAccountBalance(accountDto, new BigDecimal("50"), isSender);
        // then
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("50"));
    }

    @DisplayName("Should update an account balance adding the amount")
    @Test
    void shouldUpdateAccountBalanceAddingAmount() {
        // given
        boolean isSender = false;
        // when
        when(mapper.toEntity(accountDto)).thenReturn(account);
        when(repository.save(any())).thenReturn(account);
        service.updateAccountBalance(accountDto, new BigDecimal("50"), isSender);
        // then
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("150"));
    }

    @DisplayName("Should throw BalanceException if has no balance")
    @Test
    void shouldThrowBalanceExceptionIfHasNoBalance() {
        assertThrows(BalanceException.class, () -> service.hasEnoughBalance(accountDto));
    }

}