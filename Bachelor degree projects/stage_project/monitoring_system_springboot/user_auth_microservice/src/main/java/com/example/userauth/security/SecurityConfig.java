package com.example.userauth.security;

import com.example.userauth.filter.CustomAuthenticationFilter;
import com.example.userauth.filter.CustomAuthorizationFilter;
import com.example.userauth.util.Costant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Costant costant;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // settiamo l'autentication manager dove gli diciamo come ottenere l'oggetto userDetails
        // e che encoder di password stiamo usando
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors();

        // stiamo dicendo che la sessione è stateless così all'utente
        // non verrà assegnato un cookie
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        // stiamo dicendo che qualsiasi utente ha accesso chiunque anche se non loggato
        /*http.authorizeRequests().antMatchers("/api/saluta/**").permitAll();
        http.authorizeRequests().antMatchers("/api/group/**").permitAll();
        http.authorizeRequests().antMatchers("/api/groups/**").permitAll();
        http.authorizeRequests().antMatchers("/api/roles/reactObj/**").permitAll();
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();
        // http.authorizeRequests().antMatchers("/api/roles/**").permitAll();


        // stiamo dicebdo che le operazioni di get verso questo URL possono essere fatte solo da
        // utenti che hanno un determinato ruolo
        http.authorizeRequests().antMatchers(PUT,"/api/update/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/roles/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/usersCheckMk/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        // http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");


        // http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        // per tutte le altre richieste che facciamo necessitano dell'autenticazione
        http.authorizeRequests().anyRequest().authenticated();
*/
        // noi abbiamo creato una versione custom di un filtro che estende
        //UsernamePasswordAuthenticationFilter.class facendo  http.addFilter(customAuthenticationFilter);
        // stiamo dicendo a spring security di usare questo filtro al posto di quello di default

        http.addFilter(customAuthenticationFilter);
        // qui gli stiamo dicendo prima di eseguire customAuthenticationFilter
        // esegui customAuthorizationFilter
       // http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(costant.getREACT_URL()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
