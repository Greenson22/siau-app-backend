package com.sttis.controllers;

import com.sttis.dto.DosenBiodataUpdateDTO;
import com.sttis.dto.DosenDTO;
import com.sttis.services.DosenService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dosen")
public class DosenController {

    private final DosenService dosenService;

    public DosenController(DosenService dosenService) {
        this.dosenService = dosenService;
    }

    @GetMapping
    public ResponseEntity<Page<DosenDTO>> getAllDosen(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer jurusan_id
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<DosenDTO> dosenPage = dosenService.getAllDosen(pageable, search, jurusan_id);
        return ResponseEntity.ok(dosenPage);
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