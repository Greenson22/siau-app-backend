package com.sttis.services;

import com.sttis.dto.AdminProfileDTO;
import com.sttis.models.entities.User;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminProfileDTO getAdminProfile(String username) {
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        // Memastikan user ini adalah admin
        if (!"Admin".equalsIgnoreCase(adminUser.getRole().getRoleName())) {
            throw new SecurityException("User is not an administrator.");
        }

        AdminProfileDTO dto = new AdminProfileDTO();
        dto.setNamaLengkap("Admin Sistem"); // Data bisa diperluas di masa depan
        dto.setNip("ADMIN001");
        dto.setJabatan(adminUser.getRole().getRoleName());
        dto.setEmail("admin@sttis.ac.id");
        dto.setTeleponKantor("+62 123 4567 890");
        dto.setLokasiKantor("Gedung Rektorat, Lantai 2");
        dto.setFotoProfil("https://placehold.co/128x128/A78BFA/4C1D95?text=AD");

        return dto;
    }
}