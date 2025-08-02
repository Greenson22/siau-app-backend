package com.sttis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Nonaktifkan CSRF (umum untuk API stateless)
            .csrf(csrf -> csrf.disable())
            
            // 2. Atur otorisasi untuk setiap request
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers("/api/users").permitAll() // Izinkan endpoint registrasi diakses publik
                // .anyRequest().authenticated() // Semua request lain harus terautentikasi (login)

                // Izinkan POST /api/users (untuk registrasi) diakses publik
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() 
                
                // BARU: Izinkan GET /api/users diakses publik
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mahasiswa").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mahasiswa/{id}").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mahasiswa/{id}/biodata").permitAll() 
                
                // Semua request lain selain yang di atas harus login
                .anyRequest().authenticated()
            )
            
            // 3. Aktifkan form login bawaan dari Spring Security
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}