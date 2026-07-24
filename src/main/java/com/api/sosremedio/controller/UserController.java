package com.api.sosremedio.controller;

import com.api.sosremedio.DTO.MeResponse;
import com.api.sosremedio.model.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal Jwt jwt) /*@AuthenticationPrincipal pega p principal autenticado da requisição e injeta ele */{

        //Para recuperar o dados do usuario logado atraves das claims
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        List<String> roles = jwt.getClaimAsStringList("role");

        if (roles == null || roles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token without role");
        }

        UserRole role = UserRole.valueOf(roles.get(0)); //Assumindo que o usuário tenha apenas uma função

        return new MeResponse(userId, email, role);
    }

    @GetMapping("/customer-area")
    @PreAuthorize("hasRole('CUSTOMER')") //Apenas usuários com a função CUSTOMER podem acessar
    public String customerArea() {
        return "Area do cliente acessada com sucesso";
    }

    @GetMapping("/admin-area")
    @PreAuthorize("hasRole('ADMIN')") //Apenas usuários com a função ADMIN podem acessar
    public String adminArea() {
        return "Area do admin acessada com sucesso";
    }
}
