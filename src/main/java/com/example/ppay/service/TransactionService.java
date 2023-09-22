package com.example.ppay.service;

import com.example.ppay.dto.TransactionDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.dto.mapper.TransactionDtoResponse;
import com.example.ppay.dto.mapper.UserMapper;
import com.example.ppay.model.Transaction;
import com.example.ppay.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // get receiver
        var receiver = userMapper.toEntity(userService.findById(transactionDto.receiverId()));

        // validate if sender is not a merchant type
        userService.isMerchant(sender.getUserType());

        // get sender account
        var senderAccountDto = accountService.findByUserId(transactionDto.senderId());
        // validate if sender account has enough balance
        accountService.hasEnoughBalance(senderAccountDto);
        // update balance in sender account
        accountService.updateAccountBalance(senderAccountDto, transactionDto.amount(), true);

        // get receiver account
        var receiverAccountDto = accountService.findByUserId(transactionDto.receiverId());
        // update balance in receiver account
        accountService.updateAccountBalance(receiverAccountDto, transactionDto.amount(), false);

        // create transaction
        var transaction = Transaction.createTransaction(receiver, accountMapper.toEntity(receiverAccountDto));
        // save transaction
        repository.save(transaction);

        return new TransactionDtoResponse("The transfer was successfully completed");
    }
}
