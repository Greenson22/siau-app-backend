package com.sttis.controllers;

import com.sttis.dto.MataKuliahDTO;
import com.sttis.services.MataKuliahService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mata-kuliah")
public class MataKuliahController {

    private final MataKuliahService mataKuliahService;

    public MataKuliahController(MataKuliahService mataKuliahService) {
        this.mataKuliahService = mataKuliahService;
    }

    /**
     * Endpoint untuk melihat semua mata kuliah.
     * Method: GET
     * URL: /api/mata-kuliah
     */
    @GetMapping
    public ResponseEntity<List<MataKuliahDTO>> getAllMataKuliah() {
        return ResponseEntity.ok(mataKuliahService.getAllMataKuliah());
    }

    /**
     * Endpoint untuk menambah mata kuliah baru.
     * Method: POST
     * URL: /api/mata-kuliah
     */
    @PostMapping
    public ResponseEntity<MataKuliahDTO> createMataKuliah(@Valid @RequestBody MataKuliahDTO mataKuliahDTO) {
        MataKuliahDTO createdMataKuliah = mataKuliahService.createMataKuliah(mataKuliahDTO);
        return new ResponseEntity<>(createdMataKuliah, HttpStatus.CREATED);
    }
}