package org.wildcodeschool.MyBlog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigin;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //Désactivation de la protection contre les attaques CSRF
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(List.of(allowedOrigin));
                            config.setAllowedMethods(List.of("GET", "POST","PUT", "DELETE", "OPTIONS"));
                            config.setAllowedHeaders(List.of("*"));
                            config.setAllowCredentials(true);
                            return config;
                        })
                )
                .authorizeHttpRequests(auth -> auth //Définit les règles d'accès aux différents endpoints de l'application, ici auth
                        .requestMatchers("/auth/**").permitAll() //Permet l'accès public à tous les endpoints sous auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") //Accessible aux administrateurs
                        .requestMatchers("user/**").hasAnyRole("USER", "ADMIN") //Accessible aux users et admins
                        .requestMatchers(HttpMethod.GET, "articles/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "articles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "articles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "articles/**").hasRole("ADMIN")
                        .anyRequest().authenticated() //Les autres nécessitent une authentification
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Gestion des sessions en mode stateless, donc pas de conservation de l'état de l'utilisateur
                );
        return http.build(); //compile la configuration en chaine de filtre de sécurité
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
