package com.example.ppay;

import com.example.ppay.model.Account;
import com.example.ppay.model.User;
import com.example.ppay.enums.UserType;
import com.example.ppay.repository.AccountRepository;
import com.example.ppay.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class PpayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpayApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, AccountRepository accountRepository) {
        return args -> {
            userRepository.deleteAll();

            User user1 = new User();
            user1.setFullName("Jess Reed");
            user1.setCpf("31813722005");
            user1.setEmail("jess@me.com");
            user1.setPassword("password");
            user1.setUserType(UserType.ENDUSER);
            userRepository.save(user1);

            Account account1 = new Account();
            account1.setNumber(1);
            account1.setUser(user1);
            account1.setBalance(new BigDecimal(1000));
            accountRepository.save(account1);

            User user2 = new User();
            user2.setFullName("Vih Secret");
            user2.setCpf("02571378074");
            user2.setEmail("vih@me.com");
            user2.setPassword("password");
            user2.setUserType(UserType.ENDUSER);
            userRepository.save(user2);

            Account account2 = new Account();
            account2.setNumber(2);
            account2.setUser(user2);
            account2.setBalance(new BigDecimal(2000));
            accountRepository.save(account2);

            User user3 = new User();
            user3.setFullName("Mandi Moore");
            user3.setCpf("33068571065");
            user3.setEmail("mandi@me.com");
            user3.setPassword("password");
            user3.setUserType(UserType.MERCHANT);
            userRepository.save(user3);

            Account account3 = new Account();
            account3.setNumber(3);
            account3.setUser(user3);
            account3.setBalance(new BigDecimal(3000));
            accountRepository.save(account3);
        };
    }

}
