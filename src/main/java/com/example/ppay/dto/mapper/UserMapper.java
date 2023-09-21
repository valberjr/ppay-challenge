package com.example.ppay.dto.mapper;

import com.example.ppay.dto.UserDto;
import com.example.ppay.model.User;
import com.example.ppay.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getCpf(),
                user.getEmail(),
                user.getPassword(),
                String.valueOf(user.getUserType())
        );
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        var user = new User();
        if (userDto.id() != null) {
            user.setId(userDto.id());
        }
        user.setFullName(userDto.fullName());
        user.setCpf(userDto.cpf());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setUserType(convertUserTypeValue(userDto.userType()));
        return user;
    }

    public UserType convertUserTypeValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value.toUpperCase()) {
            case "ENDUSER" -> UserType.ENDUSER;
            case "MERCHANT" -> UserType.MERCHANT;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + value);
        };
    }
}
