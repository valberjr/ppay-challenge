package com.example.ppay.service;

import com.example.ppay.dto.UserDto;
import com.example.ppay.dto.mapper.UserMapper;
import com.example.ppay.enums.UserType;
import com.example.ppay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public UserDto findById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public UserDto save(UserDto userDto) throws ConstraintViolationException {
        var savedUser = repository.save(mapper.toEntity(userDto));
        return mapper.toDto(savedUser);
    }

    public void isMerchant(UserType userType) throws Exception {
        if (userType == UserType.MERCHANT) {
            throw new Exception("Operation not allowed for user " + userType);
        }
    }
}
