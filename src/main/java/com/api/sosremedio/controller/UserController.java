package com.api.sosremedio.controller;

import com.api.sosremedio.DTO.MeResponse;
import com.api.sosremedio.model.UserRole;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal Jwt jwt) /*@AuthenticationPrincipal pega p principal autenticado da requisição e injeta ele */{

        //Para recuperar o dados do usuario logado atraves das claims
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        UserRole role = UserRole.valueOf(jwt.getClaimAsString("role"));

        return new MeResponse(userId, email, role);
    }
}
