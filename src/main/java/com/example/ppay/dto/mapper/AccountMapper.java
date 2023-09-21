package com.example.ppay.dto.mapper;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    private final UserMapper userMapper;

    public AccountMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountDto(
                account.getId(),
                account.getNumber(),
                account.getBalance(),
                userMapper.toDto(account.getUser())
        );
    }

    public Account toEntity(AccountDto accountDto) {
        if (accountDto == null) {
            return null;
        }
        var account = new Account();
        if (accountDto.id() != null) {
            account.setId(accountDto.id());
        }
        account.setNumber(accountDto.number());
        account.setBalance(accountDto.balance());
        account.setUser(userMapper.toEntity(accountDto.user()));
        return account;
    }
}
