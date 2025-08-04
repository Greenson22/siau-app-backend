package com.sttis.controllers;

import com.sttis.dto.MahasiswaDiKelasDTO;
import com.sttis.dto.PresensiInputDTO;
import com.sttis.services.KelasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kelas")
public class KelasController {

    private final KelasService kelasService;

    public KelasController(KelasService kelasService) {
        this.kelasService = kelasService;
    }

    /**
     * Endpoint untuk melihat daftar mahasiswa di kelas tertentu.
     * Method: GET
     * URL: /api/kelas/{id}/mahasiswa
     */
    @GetMapping("/{id}/mahasiswa")
    public ResponseEntity<List<MahasiswaDiKelasDTO>> getMahasiswaDiKelas(@PathVariable("id") Integer kelasId, Authentication authentication) {
        String username = authentication.getName();
        List<MahasiswaDiKelasDTO> mahasiswaList = kelasService.getMahasiswaDiKelas(kelasId, username);
        return ResponseEntity.ok(mahasiswaList);
    }

    /**
     * Endpoint untuk mengisi presensi untuk pertemuan di kelasnya.
     * Method: POST
     * URL: /api/kelas/{id}/presensi
     */
    @PostMapping("/{id}/presensi")
    public ResponseEntity<String> isiPresensi(@PathVariable("id") Integer kelasId,
                                              @Valid @RequestBody PresensiInputDTO input,
                                              Authentication authentication) {
        String username = authentication.getName();
        kelasService.isiPresensi(kelasId, input, username);
        return ResponseEntity.ok("Presensi untuk pertemuan ke-" + input.getPertemuanKe() + " berhasil disimpan.");
    }
}