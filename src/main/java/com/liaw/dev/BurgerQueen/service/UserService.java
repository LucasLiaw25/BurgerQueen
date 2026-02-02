package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.TokenDTO;
import com.liaw.dev.BurgerQueen.dto.dtos.UserDTO;
import com.liaw.dev.BurgerQueen.dto.request.ActivateRequest;
import com.liaw.dev.BurgerQueen.dto.request.LoginRequest;
import com.liaw.dev.BurgerQueen.dto.request.UserRequest;
import com.liaw.dev.BurgerQueen.entity.Scope;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.exception.exceptions.UserNotFoundException;
import com.liaw.dev.BurgerQueen.exception.exceptions.UserNotVerifyException;
import com.liaw.dev.BurgerQueen.mapper.UserMapper;
import com.liaw.dev.BurgerQueen.repository.UserRepository;
import com.liaw.dev.BurgerQueen.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder encoder;
    private final UserValidator validator;
    private final UserRepository repository;
    private final EmailService emailService;

    @Transactional
    public UserDTO createUser(UserRequest request){
        validator.validateCreateUser(request);
        User user = mapper.toEntity(request);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCode(this.generateCode());
        repository.save(user);
        emailService.createUserEmail(user);
        return mapper.toDTO(user);
    }

    public void activateUser(ActivateRequest request){
        User user = repository.findByCode(request.code())
                .orElseThrow(()-> new UserNotFoundException("Código de ativação inválido"));
        user.setActivate(true);
        repository.save(user);
    }

    public TokenDTO login(LoginRequest loginRequest){
        Optional<User> findUser = repository.findByEmail(loginRequest.email());
        if (findUser.isEmpty() || !passwordMatches(loginRequest.password(), findUser.get().getPassword()) ){
            throw new BadCredentialsException("Credenciais inválidas");
        } else if (findUser.get().getActivate() == false) {
            throw new UserNotVerifyException("Conta não verificada. Por favor, ative sua conta antes de fazer login.");
        }


        User user = findUser.get();
        List<String> scopes = user.getScopes()
                .stream().map(Scope::getName).toList();

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("BurgerQueen")
                .subject(user.getEmail())
                .claim("Id", user.getId())
                .claim("Name", user.getName())
                .claim("scope", scopes)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(86400))
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();
        return new TokenDTO(token);
    }

    public Boolean passwordMatches(String password, String savedPassword){
        return encoder.matches(password, savedPassword);
    }

    public List<UserDTO> listUsers(){
        List<User> users = repository.findAll();
        return users.stream().map(mapper::toDTO).toList();
    }

    public UserDTO findById(Long id){
        validator.validateUserId(id);
        User user = repository.findById(id).get();
        return mapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, UserRequest request){
        validator.validateUserId(id);
        User user = mapper.toEntity(request);
        user.setId(request.getId());
        repository.save(user);
        return mapper.toDTO(user);
    }

    public void deleteUser(Long id){
        validator.validateUserId(id);
        repository.deleteById(id);
    }

    private String generateCode(){
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 5; i++) {
            code = code.append(alphaNumeric.charAt(random.nextInt(alphaNumeric.length())));
        }
        return code.toString();
    }

}
