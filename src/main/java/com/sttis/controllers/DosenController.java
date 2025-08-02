package com.sttis.controllers;

import com.sttis.dto.DosenBiodataUpdateDTO;
import com.sttis.dto.DosenDTO;
import com.sttis.services.DosenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dosen")
public class DosenController {

    private final DosenService dosenService;

    public DosenController(DosenService dosenService) {
        this.dosenService = dosenService;
    }

    @GetMapping
    public ResponseEntity<List<DosenDTO>> getAllDosen() {
        return ResponseEntity.ok(dosenService.getAllDosen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DosenDTO> getDosenById(@PathVariable Integer id) {
        return ResponseEntity.ok(dosenService.getDosenById(id));
    }

    @PutMapping("/{id}/biodata")
    public ResponseEntity<String> updateBiodataDosen(@PathVariable Integer id, @Valid @RequestBody DosenBiodataUpdateDTO biodataDTO) {
        dosenService.updateBiodata(id, biodataDTO);
        return ResponseEntity.ok("Biodata dosen berhasil diperbarui.");
    }
}