package com.sttis.controllers;

import com.sttis.dto.KrsDTO;
import com.sttis.dto.MahasiswaDTO;
import com.sttis.dto.MahasiswaAkademikProfileDTO;
import com.sttis.services.DosenPaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dosen/pa")
public class DosenPaController {

    private final DosenPaService dosenPaService;

    public DosenPaController(DosenPaService dosenPaService) {
        this.dosenPaService = dosenPaService;
    }

    @GetMapping("/mahasiswa")
    public ResponseEntity<List<MahasiswaDTO>> getMahasiswaBimbingan(Authentication authentication) {
        List<MahasiswaDTO> mahasiswaList = dosenPaService.getMahasiswaBimbingan(authentication.getName());
        return ResponseEntity.ok(mahasiswaList);
    }

    @GetMapping("/mahasiswa/{id}/krs")
    public ResponseEntity<List<KrsDTO>> getKrsMahasiswaBimbingan(@PathVariable("id") Integer mahasiswaId, Authentication authentication) {
        List<KrsDTO> krsList = dosenPaService.getKrsMahasiswaBimbingan(mahasiswaId, authentication.getName());
        return ResponseEntity.ok(krsList);
    }

    /**
     * ENDPOINT BARU: Mengambil profil akademik lengkap untuk mahasiswa bimbingan.
     */
    @GetMapping("/mahasiswa/{id}/akademik")
    public ResponseEntity<MahasiswaAkademikProfileDTO> getMahasiswaAkademikProfile(@PathVariable("id") Integer mahasiswaId, Authentication authentication) {
        MahasiswaAkademikProfileDTO profile = dosenPaService.getMahasiswaAkademikProfile(mahasiswaId, authentication.getName());
        return ResponseEntity.ok(profile);
    }
}