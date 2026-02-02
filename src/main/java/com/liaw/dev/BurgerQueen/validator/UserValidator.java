package com.liaw.dev.BurgerQueen.validator;

import com.liaw.dev.BurgerQueen.dto.request.UserRequest;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.exception.exceptions.UserExistException;
import com.liaw.dev.BurgerQueen.exception.exceptions.UserNotFoundException;
import com.liaw.dev.BurgerQueen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validateCreateUser(UserRequest user){
        Optional<User> findUser = repository.findByEmail(user.getEmail());
        if (findUser.isPresent()){
            throw new UserExistException("Usuário com email " + user.getEmail() + " já cadastrado.");
        }
    }

    public void validateUserId(Long id){
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("Usuário não encontrado");
        }
    }

}
