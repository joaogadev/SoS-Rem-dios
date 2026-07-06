package com.api.sosremedio.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.aspectj.bridge.IMessage;

//não deve receber o usermodel diretamente
public record RegisterRequest(
        @NotBlank(message = "O campo 'nome' é obrigatório.")
        @Size(
                min = 3, max = 255,
                message = "O campo deve ter no mínimo 3 caracteres"
        )
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um email válido")
        @Size(message = "Email não deve ultrapassar 255 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(
                min = 8,
                message = "A senha deve ter no mínimo 8 caracteres"
        )
        String password,

        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Informe um telefone válido, contendo apenas números e opcionalmente o sinal de +"
        )
        String phone
) {
}
