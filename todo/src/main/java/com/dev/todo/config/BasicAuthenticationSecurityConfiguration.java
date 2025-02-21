package com.dev.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class BasicAuthenticationSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return
                http
                        .authorizeHttpRequests(request -> {
                            request
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .anyRequest().authenticated();
                        })
                        .httpBasic(Customizer.withDefaults())
                        .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .csrf(AbstractHttpConfigurer::disable)
                        .build();
    }
}

