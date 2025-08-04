package com.sttis.controllers;

import com.sttis.dto.NilaiInputDTO;
import com.sttis.dto.PersetujuanDTO;
import com.sttis.services.KrsManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/krs")
public class KrsManagementController {

    private final KrsManagementService krsManagementService;

    public KrsManagementController(KrsManagementService krsManagementService) {
        this.krsManagementService = krsManagementService;
    }

    @PatchMapping("/{id}/nilai")
    public ResponseEntity<String> updateNilai(@PathVariable("id") Integer krsId, @Valid @RequestBody NilaiInputDTO nilaiInput, Authentication authentication) {
        krsManagementService.updateNilai(krsId, nilaiInput, authentication.getName());
        return ResponseEntity.ok("Nilai berhasil diupdate.");
    }

    @PatchMapping("/{id}/persetujuan")
    public ResponseEntity<String> updatePersetujuan(@PathVariable("id") Integer krsId, @Valid @RequestBody PersetujuanDTO persetujuanInput, Authentication authentication) {
        krsManagementService.updateStatusPersetujuan(krsId, persetujuanInput, authentication.getName());
        return ResponseEntity.ok("Status persetujuan KRS berhasil diupdate.");
    }
}