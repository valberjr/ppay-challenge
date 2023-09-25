package com.example.ppay.service;

import com.example.ppay.dto.AccountDto;
import com.example.ppay.dto.TransactionDto;
import com.example.ppay.dto.UserDto;
import com.example.ppay.dto.mapper.AccountMapper;
import com.example.ppay.dto.mapper.TransactionDtoResponse;
import com.example.ppay.dto.mapper.UserMapper;
import com.example.ppay.enums.UserType;
import com.example.ppay.exception.BalanceException;
import com.example.ppay.exception.OperationNotAllowedException;
import com.example.ppay.model.Account;
import com.example.ppay.model.Transaction;
import com.example.ppay.model.User;
import com.example.ppay.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    AccountMapper accountMapper;
    @Mock
    AccountService accountService;
    @Mock
    UserMapper userMapper;
    @Mock
    UserService userService;
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    TransactionService transactionService;

    TransactionDto transactionDto;
    User sender;
    UserDto senderDto;
    User receiver;
    UserDto receiverDto;
    AccountDto senderAccountDto;
    AccountDto receiverAccountDto;
    Account receiverAccount;
    Transaction transaction;

    @BeforeEach
    public void setup() {
        transactionDto = createTransactionDto();
        sender = createSender();
        senderDto = createSenderDto();
        receiver = createReceiver();
        receiverDto = createReceiverDto();
        senderAccountDto = createSenderAccountDto("1253ff02", 1);
        receiverAccountDto = createSenderAccountDto("589gf03", 2);
        receiverAccount = createReceiverAccount();
        transaction = createTransaction();
    }

    @DisplayName("Should throw OperationNotAllowedException when user type is merchant")
    @Test
    void shouldThrowOperationNotAllowedExceptionWhenUserTypeIsMerchant() throws OperationNotAllowedException {
        // when
        when(userService.findById(transactionDto.senderId())).thenReturn(senderDto);
        when(userMapper.toEntity(senderDto)).thenReturn(sender);
        when(userService.findById(transactionDto.receiverId())).thenReturn(receiverDto);
        when(userMapper.toEntity(receiverDto)).thenReturn(receiver);
        doThrow(OperationNotAllowedException.class).when(userService).isMerchant(sender.getUserType());
        // then
        assertThrows(OperationNotAllowedException.class, () -> transactionService.sendMoney(transactionDto));
    }

    @DisplayName("Should throws BalanceException if has no balance")
    @Test
    void shouldThrowBalanceExceptionIfHasNoBalance() throws OperationNotAllowedException, BalanceException {
        // when
        when(userService.findById(transactionDto.senderId())).thenReturn(senderDto);
        when(userMapper.toEntity(senderDto)).thenReturn(sender);
        when(userService.findById(transactionDto.receiverId())).thenReturn(receiverDto);
        when(userMapper.toEntity(receiverDto)).thenReturn(receiver);
        doNothing().when(userService).isMerchant(sender.getUserType());
        when(accountService.findByUserId(transactionDto.senderId())).thenReturn(senderAccountDto);
        doThrow(BalanceException.class).when(accountService).hasEnoughBalance(senderAccountDto);
        // then
        assertThrows(BalanceException.class, () -> transactionService.sendMoney(transactionDto));
    }

    @DisplayName("Should send money")
    @Test
    void shouldSendMoney() throws OperationNotAllowedException, BalanceException {
        // twhen
        when(userService.findById(transactionDto.senderId())).thenReturn(senderDto);
        when(userMapper.toEntity(senderDto)).thenReturn(sender);
        when(userService.findById(transactionDto.receiverId())).thenReturn(receiverDto);
        when(userMapper.toEntity(receiverDto)).thenReturn(receiver);
        doNothing().when(userService).isMerchant(sender.getUserType());
        when(accountService.findByUserId(transactionDto.senderId())).thenReturn(senderAccountDto);
        doNothing().when(accountService).hasEnoughBalance(senderAccountDto);
        doNothing().when(accountService).updateAccountBalance(senderAccountDto, transactionDto.amount(), true);
        when(accountService.findByUserId(transactionDto.receiverId())).thenReturn(receiverAccountDto);
        doNothing().when(accountService).updateAccountBalance(receiverAccountDto, transactionDto.amount(), false);
        when(accountMapper.toEntity(receiverAccountDto)).thenReturn(receiverAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        TransactionDtoResponse response = transactionService.sendMoney(transactionDto);
        // then
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        assertEquals("Transfer successfully completed", response.message());
    }

    private static Transaction createTransaction() {
        return Transaction.builder()
                .id("865gb04")
                .build();
    }

    private static Account createReceiverAccount() {
        return Account.builder()
                .id("1253ff02")
                .number(1)
                .balance(new BigDecimal("100"))
                .build();
    }

    private static AccountDto createSenderAccountDto(String id, int number) {
        return new AccountDto(id, number, new BigDecimal("100"), null);
    }

    private static UserDto createReceiverDto() {
        return new UserDto(
                "1543ff02",
                "Vih",
                "76513722658",
                "vih@me.com",
                "password", String.valueOf(UserType.ENDUSER)
        );
    }

    private static User createReceiver() {
        return User.builder()
                .id("3263ee01")
                .fullName("Jess")
                .cpf("31813722005")
                .email("jess@me.com")
                .password("password")
                .userType(UserType.ENDUSER)
                .build();
    }

    private static UserDto createSenderDto() {
        return new UserDto(
                "3263ee01",
                "Jess",
                "31813722005",
                "jess@me.com",
                "password", String.valueOf(UserType.ENDUSER)
        );
    }

    private static User createSender() {
        return User.builder()
                .id("3263ee01")
                .fullName("Jess")
                .cpf("31813722005")
                .email("jess@me.com")
                .password("password")
                .userType(UserType.MERCHANT)
                .build();
    }

    private static TransactionDto createTransactionDto() {
        return new TransactionDto("1", "2", new BigDecimal("100"));
    }
}