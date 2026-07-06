package com.api.sosremedio.DTO;

import com.api.sosremedio.model.UserModel;
import com.api.sosremedio.model.UserRole;

import java.util.UUID;

//não deve retornar usemodel diretamente
public record UserResponse(
        UUID id,
        String nome,
        String emial,
        String phone,
        UserRole role
) {
    //atribui valores userModel a entidade segura
    public static UserResponse from(UserModel user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

}
