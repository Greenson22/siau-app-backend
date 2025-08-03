package com.sttis.controllers;

import com.sttis.dto.KrsDTO;
import com.sttis.services.KrsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mahasiswa/me/krs")
public class KrsController {

    private final KrsService krsService;

    public KrsController(KrsService krsService) {
        this.krsService = krsService;
    }

    /**
     * Endpoint untuk melihat KRS milik sendiri.
     * Method: GET
     * URL: /api/mahasiswa/me/krs
     */
    @GetMapping
    public ResponseEntity<List<KrsDTO>> getMyKrs(Authentication authentication) {
        String username = authentication.getName();
        List<KrsDTO> krsList = krsService.getMyKrs(username);
        return ResponseEntity.ok(krsList);
    }

    /**
     * Endpoint untuk menambahkan kelas baru ke KRS.
     * Request Body harus berisi: { "kelasId": <id_kelas> }
     * Method: POST
     * URL: /api/mahasiswa/me/krs
     */
    @PostMapping
    public ResponseEntity<KrsDTO> addKelasToMyKrs(@RequestBody Map<String, Integer> payload, Authentication authentication) {
        String username = authentication.getName();
        Integer kelasId = payload.get("kelasId");
        if (kelasId == null) {
            return ResponseEntity.badRequest().build();
        }
        KrsDTO newKrs = krsService.addKelasToMyKrs(kelasId, username);
        return new ResponseEntity<>(newKrs, HttpStatus.CREATED);
    }

    /**
     * Endpoint untuk menghapus kelas dari KRS.
     * Method: DELETE
     * URL: /api/mahasiswa/me/krs/{krs_id}
     */
    @DeleteMapping("/{krs_id}")
    public ResponseEntity<Void> deleteKrs(@PathVariable("krs_id") Integer krsId, Authentication authentication) {
        String username = authentication.getName();
        krsService.deleteKrs(krsId, username);
        return ResponseEntity.noContent().build();
    }
}