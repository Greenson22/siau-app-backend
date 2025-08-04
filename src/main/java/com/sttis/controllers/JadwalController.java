package com.sttis.controllers;

import com.sttis.dto.JadwalUjianDTO;
import com.sttis.dto.KelasDTO;
import com.sttis.services.JadwalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JadwalController {

    private final JadwalService jadwalService;

    public JadwalController(JadwalService jadwalService) {
        this.jadwalService = jadwalService;
    }

    /**
     * Endpoint untuk melihat semua jadwal ujian yang aktif.
     * Method: GET
     * URL: /api/jadwal/ujian
     */
    @GetMapping("/jadwal/ujian")
    public ResponseEntity<List<JadwalUjianDTO>> getJadwalUjian() {
        List<JadwalUjianDTO> jadwal = jadwalService.getJadwalUjianAktif();
        return ResponseEntity.ok(jadwal);
    }

    /**
     * Endpoint untuk melihat daftar kelas yang ditawarkan.
     * Method: GET
     * URL: /api/kelas
     */
    @GetMapping("/kelas")
    public ResponseEntity<List<KelasDTO>> getKelas() {
        List<KelasDTO> kelas = jadwalService.getKelasDitawarkan();
        return ResponseEntity.ok(kelas);
    }
}