package com.api.sosremedio.services;

import com.api.sosremedio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //gera construtor para final's
public class UserDetailServices {
    private final UserRepository userRepository;

    //encontra e devolve o UserModel que implementa o userDetails
    public UserDetails loadUserByUsername(String username) {
        String normalizedEmail = username.trim().toLowerCase();

        return userRepository.findByEmail(normalizedEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + normalizedEmail));
    }

}
