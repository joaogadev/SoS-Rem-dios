package com.api.sosremedio.controller;

import com.api.sosremedio.DTO.RegisterRequest;
import com.api.sosremedio.DTO.UserResponse;
import com.api.sosremedio.services.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServices authServices;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authServices.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth público funcionando");
    }

}
