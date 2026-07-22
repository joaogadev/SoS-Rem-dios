package com.api.sosremedio.services;

import com.api.sosremedio.DTO.LoginResponse;
import com.api.sosremedio.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationMinutes;

    public TokenService (
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer=sos-remedio-api}") String issuer,
            @Value("${app.jwt.expiration-minutes=60}") long expirationMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public LoginResponse generatedToken(UserModel userModel) {
        Instant now  = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationMinutes * 60);

        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userModel.getId().toString())
                .claim("email", userModel.getEmail())
                .claim("role", userModel.getRole().name())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(header, claim))
                .getTokenValue();

        long expiressInSecond = expirationMinutes * 60;

        return new LoginResponse(
                token, "Barear", expiressInSecond
        );
    }
}
