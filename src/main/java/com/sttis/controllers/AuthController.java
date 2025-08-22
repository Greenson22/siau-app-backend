package com.sttis.controllers;

import com.sttis.config.JwtUtil;
import com.sttis.dto.*;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository, MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
    }

    // Helper sederhana untuk mendapatkan inisial
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        return Arrays.stream(name.split(" "))
                     .filter(s -> !s.isEmpty())
                     .map(s -> s.substring(0, 1))
                     .collect(Collectors.joining())
                     .toUpperCase();
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDTO loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String jwt = jwtUtil.generateToken(userDetails);

        UserProfileDTO userProfile = getMyProfile(authentication).getBody();

        return ResponseEntity.ok(new LoginResponseDTO(jwt, userProfile));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUserId(user.getUserId());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setRole(user.getRole().getRoleName());

        if ("Mahasiswa".equalsIgnoreCase(user.getRole().getRoleName())) {
            Mahasiswa mhs = mahasiswaRepository.findById(user.getMahasiswa().getMahasiswaId()).orElse(null);
            if (mhs != null) {
                MahasiswaDTO mhsDto = new MahasiswaDTO();
                mhsDto.setMahasiswaId(mhs.getMahasiswaId());
                mhsDto.setNim(mhs.getNim());
                mhsDto.setNamaLengkap(mhs.getNamaLengkap());
                mhsDto.setStatus(mhs.getStatus().name());

                if (mhs.getJurusan() != null) {
                    mhsDto.setNamaJurusan(mhs.getJurusan().getNamaJurusan());
                }
                
                if (mhs.getPembimbingAkademik() != null) {
                    mhsDto.setNamaDosenPA(mhs.getPembimbingAkademik().getNamaLengkap());
                } else {
                    mhsDto.setNamaDosenPA("Belum Ditentukan");
                }
                
                // Membuat URL foto profil di backend
                String inisial = getInitials(mhs.getNamaLengkap());
                String fotoUrl = "https://placehold.co/128x128/FCA5A5/991B1B?text=" + inisial;
                mhsDto.setFotoProfil(fotoUrl);
                
                profileDTO.setMahasiswaInfo(mhsDto);
            }
        } else if ("Dosen".equalsIgnoreCase(user.getRole().getRoleName())) {
            Dosen dosen = dosenRepository.findById(user.getDosen().getDosenId()).orElse(null);
            if (dosen != null) {
                DosenDTO dosenDto = new DosenDTO();
                dosenDto.setDosenId(dosen.getDosenId());
                dosenDto.setNidn(dosen.getNidn());
                dosenDto.setNamaLengkap(dosen.getNamaLengkap());
                profileDTO.setDosenInfo(dosenDto);
            }
        }

        return ResponseEntity.ok(profileDTO);
    }
}