package com.liaw.dev.BurgerQueen.dto.request;

import com.liaw.dev.BurgerQueen.entity.Scope;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    @NotBlank(message = "Nome obrigatório")
    private String name;
    @NotBlank(message = "Número de telefone obrigatório")
    private String phone;
    @NotBlank(message = "Email obrigatório")
    private String email;
    @CPF
    @NotBlank(message = "CPF obrigatório")
    private String cpf;
    @NotBlank(message = "Senha obrigatória")
    private String password;
    private List<Scope> scopes;

}
