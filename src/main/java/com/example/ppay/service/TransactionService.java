package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.TransactionDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.dto.mapper.TransactionDtoResponse;
import com.example.ppay.dto.mapper.UserMapper;
import com.example.ppay.enums.TransactionType;
import com.example.ppay.enums.UserType;
import com.example.ppay.model.Account;
import com.example.ppay.model.Transaction;
import com.example.ppay.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TransactionRepository repository;

    @Transactional
    public TransactionDtoResponse sendMoney(TransactionDto transactionDto) throws Exception {
        // get sender
        var sender = userMapper.toEntity(userService.findById(transactionDto.senderId()));
        // validate sender is a merchant
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Operation not allowed for user " + sender.getUserType());
        }

        // get sender account
        Optional<AccountDto> senderAccountOpt = accountService.findByUserId(transactionDto.senderId());
        if (senderAccountOpt.isEmpty()) {
            throw new Exception("User account " + transactionDto.senderId() + " not found");
        }
        // validate sender account has enough balance
        if (senderAccountOpt.get().balance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Not enough balance");
        }

        // get receiver
        var receiver = userMapper.toEntity(userService.findById(transactionDto.receiverId()));

        // get receiver account
        Optional<AccountDto> receiverAccountOpt = accountService.findByUserId(transactionDto.receiverId());
        if (receiverAccountOpt.isEmpty()) {
            throw new Exception("User account " + transactionDto.receiverId() + " not found");
        }

        // update balance in receiver account
        Account receiverAccount = accountMapper.toEntity(receiverAccountOpt.get());
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDto.amount()));
        accountService.updateAccountBalance(accountMapper.toDto(receiverAccount));

        // update balance in sender account
        Account senderAccount = accountMapper.toEntity(senderAccountOpt.get());
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDto.amount()));
        accountService.updateAccountBalance(accountMapper.toDto(senderAccount));

        // save transaction
        var transaction = new Transaction();
        transaction.setUser(receiver);
        transaction.setAccount(receiverAccount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setCreatedAt(LocalDateTime.now());
        repository.save(transaction);

        return new TransactionDtoResponse("The transfer wass successfuly completed");
    }
}
