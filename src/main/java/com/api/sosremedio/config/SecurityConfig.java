package com.api.sosremedio.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.DispatcherType;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Bean
    //Transforma chave do properties em chava entendivel pelo java
    //reusmindo um conversão, ja q o o secretKey nn recebe string
    public SecretKey jwtSecretkey() {
        byte[] keyBytes = Base64
                .getDecoder()
                .decode(jwtSecret);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }


    @Bean
    //esse cara válida todo o processo de login
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    //gera token jwt
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        ImmutableSecret<SecurityContext> secret = new ImmutableSecret<>(jwtSecretKey);
        return new NimbusJwtEncoder(secret);
    }

    @Bean
    //válida token recebido
    public JwtDecoder jwtDencoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    //criptografa e válida senhas
    public PasswordEncoder passwordEncoder() {
        //codificador com BCrypt como padrão, que mantém no hash a identificação
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        System.out.println("SecurityConfig carregado");
        http
                //não necessário, pois JWT será enviado manualmente no header Authorization
                .csrf(AbstractHttpConfigurer::disable)
                //para não utilizar a página de login padrão do spring
                .formLogin(AbstractHttpConfigurer::disable)
                //para não utilizar http básico
                .httpBasic(AbstractHttpConfigurer::disable)
                //para não criar sessão, pois o JWT é stateless
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                //permite cadastro
                .authorizeHttpRequests(authorize -> authorize
                        //libera dispatch interno de erro.
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        //autoriza somente essa rota
                        .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                //responsavel por informar ao spring que a api é protegida por barear token
                .oauth2ResourceServer(oathh2 ->
                        oathh2.jwt(
                                jwt -> {}
                                //vazia para manter a configuração padrão
                        ));
        return http.build();



    }
}
