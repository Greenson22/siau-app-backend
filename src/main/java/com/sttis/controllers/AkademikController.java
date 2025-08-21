package com.sttis.controllers;

import com.sttis.dto.AkademikSummaryDTO;
import com.sttis.dto.BiodataMahasiswaDTO;
import com.sttis.dto.KhsDTO;
import com.sttis.dto.KelasDTO;
import com.sttis.dto.RekapPresensiDTO;
import com.sttis.services.AkademikService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mahasiswa/me")
public class AkademikController {

    private final AkademikService akademikService;

    public AkademikController(AkademikService akademikService) {
        this.akademikService = akademikService;
    }

    /**
     * ENDPOINT BARU untuk mengambil jadwal kuliah semester ini.
     * Method: GET
     * URL: /api/mahasiswa/me/jadwal
     */
    @GetMapping("/jadwal")
    public ResponseEntity<List<KelasDTO>> getMyJadwal(Authentication authentication) {
        String username = authentication.getName();
        List<KelasDTO> jadwal = akademikService.getMyJadwal(username);
        return ResponseEntity.ok(jadwal);
    }

    /**
     * Endpoint untuk melihat Kartu Hasil Studi (KHS) milik sendiri.
     * Method: GET
     * URL: /api/mahasiswa/me/khs
     */
    @GetMapping("/khs")
    public ResponseEntity<List<KhsDTO>> getMyKhs(Authentication authentication) {
        String username = authentication.getName();
        List<KhsDTO> khs = akademikService.getMyKhs(username);
        return ResponseEntity.ok(khs);
    }

    /**
     * Endpoint untuk melihat rekap presensi per kelas milik sendiri.
     * Method: GET
     * URL: /api/mahasiswa/me/presensi
     */
    @GetMapping("/presensi")
    public ResponseEntity<List<RekapPresensiDTO>> getMyPresensi(Authentication authentication) {
        String username = authentication.getName();
        List<RekapPresensiDTO> rekapPresensi = akademikService.getMyRekapPresensi(username);
        return ResponseEntity.ok(rekapPresensi);
    }

    /**
     * ENDPOINT BARU untuk mengambil biodata pribadi mahasiswa.
     * Method: GET
     * URL: /api/mahasiswa/me/biodata
     */
    @GetMapping("/biodata")
    public ResponseEntity<BiodataMahasiswaDTO> getMyBiodata(Authentication authentication) {
        String username = authentication.getName();
        BiodataMahasiswaDTO biodata = akademikService.getMyBiodata(username);
        return ResponseEntity.ok(biodata);
    }

    /**
     * ENDPOINT BARU untuk mengambil ringkasan akademik (IPK dan Total SKS).
     * Method: GET
     * URL: /api/mahasiswa/me/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<AkademikSummaryDTO> getMyAkademikSummary(Authentication authentication) {
        String username = authentication.getName();
        AkademikSummaryDTO summary = akademikService.getAkademikSummary(username);
        return ResponseEntity.ok(summary);
    }
}