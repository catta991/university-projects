package com.example.resourceservercustom.security;

import com.example.resourceservercustom.filter.Filter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final Filter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeHttpRequests().requestMatchers("/admin/resources").hasAnyAuthority("ADMIN");

        // Role_user è il nome del ruolo utente che usa oauth
        http.authorizeHttpRequests().requestMatchers("/user/resources").hasAnyAuthority("ADMIN", "USER", "ROLE_USER");
        http.authorizeHttpRequests().anyRequest().authenticated();

        // prima del filtro UsernamePasswordAuthentication viene eseguito il filtro custom
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
