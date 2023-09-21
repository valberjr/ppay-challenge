package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Optional<AccountDto> findByUserId(String userId) {
        return accountRepository.findByUserId(userId)
                .map(accountMapper::toDto);
    }

    @Transactional
    public void updateAccountBalance(AccountDto accountDto) {
        accountRepository.save(accountMapper.toEntity(accountDto));
    }
}
