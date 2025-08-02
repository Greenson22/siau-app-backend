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
            // 1. Nonaktifkan CSRF
            .csrf(csrf -> csrf.disable())
            
            // 2. Atur otorisasi untuk setiap request
            .authorizeHttpRequests(auth -> auth
                // Izinkan akses publik untuk endpoint yang sudah ada
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mahasiswa").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/mahasiswa/{id}").permitAll() 
                .requestMatchers(HttpMethod.PUT, "/api/mahasiswa/{id}/biodata").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/dosen").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/dosen/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/dosen/{id}/biodata").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jurusan").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/jurusan").permitAll()

                // BARU: Izin untuk endpoint mata kuliah
                .requestMatchers(HttpMethod.GET, "/api/mata-kuliah").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/mata-kuliah").permitAll()
                
                // Semua request lain harus diautentikasi
                .anyRequest().authenticated()
            )
            
            // 3. Aktifkan form login bawaan
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}