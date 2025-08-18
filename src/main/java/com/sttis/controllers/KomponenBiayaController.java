package com.sttis.controllers;

import com.sttis.dto.KomponenBiayaDTO;
import com.sttis.services.KeuanganService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/komponen-biaya")
public class KomponenBiayaController {

    private final KeuanganService keuanganService;

    public KomponenBiayaController(KeuanganService keuanganService) {
        this.keuanganService = keuanganService;
    }

    @GetMapping
    public ResponseEntity<List<KomponenBiayaDTO>> getAllKomponenBiaya() {
        List<KomponenBiayaDTO> komponenList = keuanganService.getAllKomponenBiaya();
        return ResponseEntity.ok(komponenList);
    }
}