package com.api.sosremedio.services;

import com.api.sosremedio.DTO.LoginRequest;
import com.api.sosremedio.DTO.LoginResponse;
import com.api.sosremedio.DTO.RegisterRequest;
import com.api.sosremedio.DTO.UserResponse;
import com.api.sosremedio.model.UserModel;
import com.api.sosremedio.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        //Criptografia
        String passwordHash = passwordEncoder.encode(request.password());

        String normalizedPhone = normalizePhone(request.phone());
        UserModel user = new UserModel(request.name().trim(), request.email(), passwordHash, normalizedPhone);
        UserModel savedUser = userRepository.save(user);

        //retorna entidade segura com valores do userModel
        return UserResponse.from(savedUser);
    }

    public LoginResponse Login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        try {
            //Spring tentará autenticar esse email com a respectiva senha
            UsernamePasswordAuthenticationToken autenticationToken = new UsernamePasswordAuthenticationToken(
                    normalizedEmail, request.password()
            );

            //Onde o real fluxo de validação ocorre, chamando o UserDetailService para buscar o usuário e validar a senha
            Authentication authentication = authenticationManager.authenticate(autenticationToken);

            UserModel user = (UserModel) authentication.getPrincipal();

            return tokenService.generatedToken(user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        return phone.trim();
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

