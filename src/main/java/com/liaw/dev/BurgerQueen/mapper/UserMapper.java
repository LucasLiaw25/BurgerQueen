package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.UserDTO;
import com.liaw.dev.BurgerQueen.dto.request.UserRequest;
import com.liaw.dev.BurgerQueen.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getCpf(),
                user.getPassword(),
                user.getCode(),
                user.getActivate()
        );
    }

    public User toEntity(UserRequest user){
        return new User(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getCpf(),
                user.getPassword(),
                user.getScopes()
        );
    }

}
