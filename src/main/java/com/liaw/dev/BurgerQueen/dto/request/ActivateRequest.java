package com.liaw.dev.BurgerQueen.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ActivateRequest(
       @NotBlank(message = "Código precisa ser preenchido") String code
) {
}
