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
                
                // --- PERBAIKAN DI SINI ---
                // Cek jika Dosen PA ada, lalu set namanya.
                if (mhs.getPembimbingAkademik() != null) {
                    mhsDto.setNamaDosenPA(mhs.getPembimbingAkademik().getNamaLengkap());
                } else {
                    mhsDto.setNamaDosenPA("Belum Ditentukan");
                }
                
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