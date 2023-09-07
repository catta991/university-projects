package com.example.authorization.security;

import com.example.authorization.filter.CustomFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/auth/user/login").permitAll();
        http.authorizeRequests().antMatchers("/auth/user/roles").permitAll();
        http.authorizeRequests().antMatchers("/auth/user/refreshToken").permitAll();
        http.authorizeRequests().antMatchers("/auth/user/get/secret/code").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_USER");;
        http.authorizeRequests().antMatchers("/auth/host/getHosts").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_USER");

        http.authorizeRequests().antMatchers("/auth/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }





}
