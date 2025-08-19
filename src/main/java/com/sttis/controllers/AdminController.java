package com.sttis.controllers;

import com.sttis.dto.AdminProfileDTO;
import com.sttis.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Endpoint untuk mengambil data profil admin yang sedang login.
     */
    @GetMapping("/me/profile")
    public ResponseEntity<AdminProfileDTO> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        AdminProfileDTO profile = adminService.getAdminProfile(username);
        return ResponseEntity.ok(profile);
    }
}