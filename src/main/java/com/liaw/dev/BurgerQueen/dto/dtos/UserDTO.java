package com.liaw.dev.BurgerQueen.dto.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liaw.dev.BurgerQueen.entity.Order;
import com.liaw.dev.BurgerQueen.entity.Scope;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String cpf;
    private String password;
    private List<Order> orders;
    private String code;
    private Boolean activate = false;

    public UserDTO(Long id, String name, String phone, String email, String cpf, String password, String code, Boolean activate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.code = code;
        this.activate = false;
    }
}
