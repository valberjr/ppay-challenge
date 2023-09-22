package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto findByUserId(String userId) throws Exception {
        Optional<AccountDto> accountOpt = accountRepository.findByUserId(userId)
                .map(accountMapper::toDto);
        if (accountOpt.isEmpty()) {
            throw new Exception("User account " + userId + " not found");
        }
        return accountOpt.get();
    }

    @Transactional
    public void updateAccountBalance(AccountDto accountDto, BigDecimal amount, boolean isSender) throws Exception {
        var senderAccount = accountMapper.toEntity(accountDto);
        if (isSender) {
            senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        } else {
            senderAccount.setBalance(senderAccount.getBalance().add(amount));
        }
        accountRepository.save(senderAccount);
    }

    public void hasEnoughBalance(AccountDto accountDto) throws Exception {
        var hasBalance = accountDto.balance().compareTo(BigDecimal.ZERO) <= 0;
        if (hasBalance) {
            throw new Exception("Not enough balance");
        }
    }
}
