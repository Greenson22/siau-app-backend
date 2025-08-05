package com.sttis.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     private final JwtRequestFilter jwtRequestFilter;

//     public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
//         this.jwtRequestFilter = jwtRequestFilter;
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // 1. Endpoint Publik (tidak perlu login)
//                 .requestMatchers("/api/auth/login").permitAll()
//                 .requestMatchers("/", "/index.html", "/akademik.html", "/dosen-akademik.html", "/keuangan.html", "/kelas.html").permitAll() // Izinkan akses ke file HTML
//                 .requestMatchers("/error-page.html").permitAll()

//                 // 2. Endpoint Umum (hanya perlu login, role apa pun)
//                 .requestMatchers("/api/auth/me").authenticated()
//                 .requestMatchers("/api/jadwal/ujian", "/api/kelas").authenticated()
//                 .requestMatchers("/api/pengumuman/**").authenticated()

//                 // 3. Endpoint Khusus Role MAHASISWA
//                 .requestMatchers("/api/mahasiswa/me/**").hasAuthority("Mahasiswa")
//                 .requestMatchers(HttpMethod.POST, "/api/pembayaran").hasAuthority("Mahasiswa")
                
//                 // 4. Endpoint Khusus Role DOSEN
//                 .requestMatchers("/api/dosen/me/**").hasAuthority("Dosen")
//                 .requestMatchers("/api/dosen/pa/**").hasAuthority("Dosen")
//                 .requestMatchers("/api/kelas/{id}/**").hasAuthority("Dosen")
//                 .requestMatchers("/api/krs/{id}/**").hasAuthority("Dosen")

//                 // 5. Endpoint Khusus Role ADMIN
//                 .requestMatchers("/api/tagihan/**").hasAuthority("Admin")
//                 .requestMatchers(HttpMethod.GET, "/api/pembayaran").hasAuthority("Admin")
//                 .requestMatchers("/api/pembayaran/{id}/verifikasi").hasAuthority("Admin")
//                 .requestMatchers("/api/users/**").hasAuthority("Admin")
//                 .requestMatchers("/api/mahasiswa/**").hasAuthority("Admin")
//                 .requestMatchers("/api/dosen/**").hasAuthority("Admin")
//                 .requestMatchers("/api/jurusan/**").hasAuthority("Admin")
//                 .requestMatchers("/api/mata-kuliah/**").hasAuthority("Admin")
//                 .requestMatchers("/api/logs/activity").hasAuthority("Admin")
                
//                 // Semua request lain harus diautentikasi
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         // Tambahkan filter JWT sebelum filter Spring Security standar
//         http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }


// Import UserDetailsServiceImpl tidak lagi diperlukan di sini
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Hapus field userDetailsService
    private final JwtRequestFilter jwtRequestFilter;

    // Hapus parameter UserDetailsServiceImpl dari constructor
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // // Endpoint publik
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Registrasi tetap publik
                
                // Endpoint yang memerlukan autentikasi
                // Semua request lain harus diautentikasi
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Tambahkan filter JWT sebelum filter Spring Security standar
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// package com.sttis.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             // 1. Nonaktifkan CSRF
//             .csrf(csrf -> csrf.disable())
            
//             // 2. Atur otorisasi untuk setiap request
//             .authorizeHttpRequests(auth -> auth
//                 // Izinkan akses publik untuk endpoint yang sudah ada
//                 .requestMatchers(HttpMethod.POST, "/api/users").permitAll() 
//                 .requestMatchers(HttpMethod.GET, "/api/users").permitAll() 
//                 .requestMatchers(HttpMethod.GET, "/api/mahasiswa").permitAll() 
//                 .requestMatchers(HttpMethod.GET, "/api/mahasiswa/{id}").permitAll() 
//                 .requestMatchers(HttpMethod.PUT, "/api/mahasiswa/{id}/biodata").permitAll() 
//                 .requestMatchers(HttpMethod.GET, "/api/dosen").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/dosen/{id}").permitAll()
//                 .requestMatchers(HttpMethod.PUT, "/api/dosen/{id}/biodata").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/jurusan").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/jurusan").permitAll()

//                 // BARU: Izin untuk endpoint mata kuliah
//                 .requestMatchers(HttpMethod.GET, "/api/mata-kuliah").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/mata-kuliah").permitAll()
                
//                 // Semua request lain harus diautentikasi
//                 .anyRequest().authenticated()
//             )
            
//             // 3. Aktifkan form login bawaan
//             .formLogin(Customizer.withDefaults());

//         return http.build();
//     }
// }