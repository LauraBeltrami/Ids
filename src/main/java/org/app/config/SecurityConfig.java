package org.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disabilita CSRF (Fondamentale per le chiamate API REST/Postman)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configura le regole di accesso (Autorizzazione)
                .authorizeHttpRequests(auth -> auth
                        // Animatori
                        .requestMatchers("/api/animatori/**").hasRole("ANIMATORE")

                        // Venditori (e Distributori)
                        .requestMatchers("/api/venditori/**").hasAnyRole("TRASFORMATORE", "VENDITORE")
                        .requestMatchers("/api/distributori/**").hasRole("VENDITORE") // Se usi distributori

                        // Acquirenti
                        .requestMatchers("/api/acquirenti/**").hasRole("ACQUIRENTE")

                        // Curatori (se hai controller per loro)
                        .requestMatchers("/api/curatori/**").hasRole("CURATORE")

                        // Eventi: Lettura pubblica (GET), Scrittura protetta (se servisse)
                        .requestMatchers(HttpMethod.GET, "/api/eventi/**").permitAll() // Tutti possono vedere gli eventi

                        // Catalogo: lettura pubblica (GET)
                        .requestMatchers(HttpMethod.GET, "/api/catalogo/**").permitAll()

                        // H2 Console (se la usi, serve per permettere l'accesso)
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers("/api/auth/register/**").permitAll()
                        // Tutto il resto richiede autenticazione
                        .anyRequest().authenticated()
                )

                .userDetailsService(customUserDetailsService) // <-- DICIAMO DI USARE IL NOSTRO SERVICE
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}