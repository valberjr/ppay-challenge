package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.AccountResponseDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.exception.BalanceException;
import com.example.ppay.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountResponseDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .map(accountMapper::toResponseDto)
                .toList();
    }

    public AccountDto findByUserId(String userId) {
        return accountRepository.findByUserId(userId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User account not found"));
    }

    @Transactional
    public void updateAccountBalance(AccountDto accountDto, BigDecimal amount, boolean isSender) {
        var senderAccount = accountMapper.toEntity(accountDto);
        if (isSender) {
            senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        } else {
            senderAccount.setBalance(senderAccount.getBalance().add(amount));
        }
        accountRepository.save(senderAccount);
    }

    public void hasEnoughBalance(AccountDto accountDto) throws BalanceException {
        var noBalance = accountDto.balance().compareTo(BigDecimal.ZERO) <= 0;
        if (noBalance) {
            throw new BalanceException("Not enough balance");
        }
    }
}
