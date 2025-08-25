package com.sttis.controllers;

import com.sttis.dto.DosenDashboardSummaryDTO;
import com.sttis.dto.DosenProfileDTO; // <-- IMPORT BARU
import com.sttis.dto.KelasDTO;
import com.sttis.services.DosenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dosen/me")
public class DosenMeController {

    private final DosenService dosenService;

    public DosenMeController(DosenService dosenService) {
        this.dosenService = dosenService;
    }
    
    /**
     * ENDPOINT BARU: Mengambil profil lengkap dosen yang login.
     */
    @GetMapping("/profile")
    public ResponseEntity<DosenProfileDTO> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        DosenProfileDTO profile = dosenService.getDosenProfile(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/kelas")
    public ResponseEntity<List<KelasDTO>> getKelasDiampu(Authentication authentication) {
        String username = authentication.getName();
        List<KelasDTO> kelasList = dosenService.getKelasDiampu(username);
        return ResponseEntity.ok(kelasList);
    }

    @GetMapping("/summary")
    public ResponseEntity<DosenDashboardSummaryDTO> getDashboardSummary(Authentication authentication) {
        String username = authentication.getName();
        DosenDashboardSummaryDTO summary = dosenService.getDosenDashboardSummary(username);
        return ResponseEntity.ok(summary);
    }
}