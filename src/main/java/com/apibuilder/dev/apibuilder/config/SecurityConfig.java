package com.apibuilder.dev.apibuilder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apibuilder.dev.apibuilder.filters.JwtFilter;
import com.apibuilder.dev.apibuilder.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin("http://localhost:4200");  // Frontend URL
                    config.addAllowedMethod("GET");
                    config.addAllowedMethod("POST");
                    config.addAllowedMethod("PUT");
                    config.addAllowedMethod("DELETE");
                    config.addAllowedMethod("OPTIONS");  // Allow OPTIONS for preflight requests
                    config.addAllowedHeader("Authorization");  // Allow the Authorization header
                    config.addAllowedHeader("Content-Type");  // Allow the Content-Type header
                    return config;
                }))
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless JWT authentication
                .authorizeHttpRequests(request -> request
                    .requestMatchers("/api/users/login", "/api/users/register")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
