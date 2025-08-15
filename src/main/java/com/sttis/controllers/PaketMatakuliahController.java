package com.sttis.controllers;

import com.sttis.dto.PaketMatakuliahDTO;
import com.sttis.services.PaketMatakuliahService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaketMatakuliahController {

    private final PaketMatakuliahService paketMatakuliahService;

    public PaketMatakuliahController(PaketMatakuliahService paketMatakuliahService) {
        this.paketMatakuliahService = paketMatakuliahService;
    }

    /**
     * Endpoint untuk mahasiswa melihat paket KRS yang disarankan untuk semester ini.
     * Method: GET
     * URL: /api/mahasiswa/me/paket-krs
     */
    @GetMapping("/mahasiswa/me/paket-krs")
    public ResponseEntity<PaketMatakuliahDTO> getMyKrsPackage(Authentication authentication) {
        String username = authentication.getName();
        PaketMatakuliahDTO paket = paketMatakuliahService.getPaketForCurrentMahasiswa(username);
        return ResponseEntity.ok(paket);
    }
}