package com.oauth.implementation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.oauth.implementation.service.DefaultUserService;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.*;

@Configuration
@EnableWebSecurity
// Classe di configurazione dello spring security
public class SpringSecurityConfig {

    @Autowired
    private DefaultUserService userDetailsService;

    /*
     * Istanza della classe AuthneticationSuccessHandler che viene utilizzata quando il
     * login va a buon fine per generare i token jwt e memorizzare l'utente loggato
     * nello spring security context
     * */
    @Autowired
    AuthenticationSuccessHandler successHandler;


    /*
     * Oggetto usato per criptare le password memorizzate nel database
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Metodo che permette di creare e configuarare il DaoAuthenticationProvider
     * che si occuperà di recuperare le informazioni degli utenti da loggare nel db
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * metodo di configurazione dello spring security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // crea un token da inserire nelle richieste Post per evitare attacchi csrf
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()

                /* tutte le richieste che verso la risorsa /admin possono essere effettuate solo da utenti
                 * con ruolo admin.
                 * Tutte le richieste verso le risorse /registration e /login possono essere efftuate da chiunque
                 * Per tutte le altre richieste l'utente deve essere autenticato
                 */
                .authorizeRequests()
                .antMatchers("/admin").hasAnyAuthority("ADMIN")
                .antMatchers("/registration", "/login", "/loginKeycloack", "/loginKeycloackCheck").permitAll()
                .anyRequest().authenticated()

                .and()
                // setta la pagina di login e l'handler in caso di login corretto
                .formLogin().loginPage("/login")
                .successHandler(successHandler)
                .and()
                .oauth2Client().and()
                /*setta la pagina di logout, invalida la sessione HTTP, cancella i cookies, pulisce lo spring security context e
                 * reindirizza alla pagina di login
                 */
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login").deleteCookies("JSESSIONID", "XSRF-TOKEN").clearAuthentication(true)
                .invalidateHttpSession(true)
                // abilita il login mediante OAuth2.0
                .and().oauth2Login()
                .loginPage("/login")
                .successHandler(successHandler);

        return http.build();

    }

}
