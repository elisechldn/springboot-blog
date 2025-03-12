package org.wildcodeschool.MyBlog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.wildcodeschool.MyBlog.controller.AuthController;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //Désactivation de la protection contre les attaques CSRF
                .authorizeHttpRequests(auth -> auth //Définit les règles d'accès aux différents endpoints de l'application, ici auth
                        .requestMatchers("/auth/**").permitAll() //Permet l'accès public à tous les endpoints sous auth
                        .anyRequest().authenticated() //Les autres nécessitent une authentification
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Gestion des sessions en mode stateless, donc pas de conservation de l'état de l'utilisateur
                );
        return http.build(); //compile la configuration en chaine de filtre de sécurité
    }
}
