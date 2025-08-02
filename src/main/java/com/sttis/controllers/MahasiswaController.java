package com.sttis.controllers;

import com.sttis.dto.BiodataUpdateDTO;
import com.sttis.dto.MahasiswaDTO;
import com.sttis.services.MahasiswaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mahasiswa")
public class MahasiswaController {

    private final MahasiswaService mahasiswaService;

    public MahasiswaController(MahasiswaService mahasiswaService) {
        this.mahasiswaService = mahasiswaService;
    }

    @GetMapping
    public ResponseEntity<List<MahasiswaDTO>> getAllMahasiswa() {
        List<MahasiswaDTO> mahasiswaList = mahasiswaService.getAllMahasiswa();
        return ResponseEntity.ok(mahasiswaList);
    }
    /**
     * Endpoint untuk melihat detail satu mahasiswa berdasarkan ID.
     * Method: GET
     * URL: /api/mahasiswa/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MahasiswaDTO> getMahasiswaById(@PathVariable Integer id) {
        MahasiswaDTO mahasiswa = mahasiswaService.getMahasiswaById(id);
        return ResponseEntity.ok(mahasiswa);
    }

    /**
     * Endpoint untuk mengubah biodata mahasiswa.
     * Method: PUT
     * URL: /api/mahasiswa/{id}/biodata
     */
    @PutMapping("/{id}/biodata")
    public ResponseEntity<String> updateBiodata(@PathVariable Integer id, @Valid @RequestBody BiodataUpdateDTO biodataDTO) {
        mahasiswaService.updateBiodata(id, biodataDTO);
        return ResponseEntity.ok("Biodata mahasiswa berhasil diperbarui.");
    }
}