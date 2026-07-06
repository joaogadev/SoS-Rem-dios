package com.api.sosremedio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEnconder() {
        //codificador com BCrypt como padrão, que mantém no hash a identificação
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
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
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/auth/register").permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();



    }
}
