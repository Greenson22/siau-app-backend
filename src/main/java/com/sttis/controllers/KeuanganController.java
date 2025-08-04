package com.sttis.controllers;

import com.sttis.dto.*;
import com.sttis.services.KeuanganService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KeuanganController {

    private final KeuanganService keuanganService;

    public KeuanganController(KeuanganService keuanganService) {
        this.keuanganService = keuanganService;
    }

    // == ENDPOINT MAHASISWA ==
    @GetMapping("/mahasiswa/me/tagihan")
    public ResponseEntity<List<TagihanDTO>> getMyTagihan(Authentication authentication) {
        return ResponseEntity.ok(keuanganService.getMyTagihan(authentication.getName()));
    }

    @PostMapping("/pembayaran")
    public ResponseEntity<String> konfirmasiPembayaran(@Valid @RequestBody PembayaranInputDTO input, Authentication authentication) {
        keuanganService.konfirmasiPembayaran(input, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Konfirmasi pembayaran berhasil, menunggu verifikasi admin.");
    }
    
    // == ENDPOINT ADMIN ==
    @GetMapping("/tagihan")
    public ResponseEntity<List<TagihanDTO>> getAllTagihan() {
        return ResponseEntity.ok(keuanganService.getAllTagihan());
    }

    @PostMapping("/tagihan")
    public ResponseEntity<TagihanDTO> createTagihan(@Valid @RequestBody TagihanInputDTO input) {
        return new ResponseEntity<>(keuanganService.createTagihan(input), HttpStatus.CREATED);
    }

    @GetMapping("/tagihan/{id}")
    public ResponseEntity<TagihanDTO> getTagihanById(@PathVariable Integer id) {
        return ResponseEntity.ok(keuanganService.getTagihanById(id));
    }

    @GetMapping("/pembayaran")
    public ResponseEntity<List<PembayaranDTO>> getAllPembayaran() {
        return ResponseEntity.ok(keuanganService.getAllPembayaran());
    }

    @PatchMapping("/pembayaran/{id}/verifikasi")
    public ResponseEntity<String> verifikasiPembayaran(@PathVariable Integer id, @Valid @RequestBody VerifikasiPembayaranDTO input) {
        keuanganService.verifikasiPembayaran(id, input);
        return ResponseEntity.ok("Status pembayaran berhasil diperbarui.");
    }
}