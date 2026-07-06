package com.api.sosremedio.services;

import com.api.sosremedio.DTO.RegisterRequest;
import com.api.sosremedio.DTO.UserResponse;
import com.api.sosremedio.model.UserModel;
import com.api.sosremedio.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        return phone.trim();
    }
}

