package com.sttis.controllers;

import com.sttis.dto.BiodataUpdateDTO;
import com.sttis.dto.MahasiswaDTO;
import com.sttis.services.MahasiswaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mahasiswa")
public class MahasiswaController {

    private final MahasiswaService mahasiswaService;

    public MahasiswaController(MahasiswaService mahasiswaService) {
        this.mahasiswaService = mahasiswaService;
    }

    /**
     * Endpoint untuk melihat daftar mahasiswa dengan paginasi dan filter.
     * Contoh: /api/mahasiswa?page=0&limit=10&search=Budi&jurusan_id=1
     */
    @GetMapping
    public ResponseEntity<Page<MahasiswaDTO>> getAllMahasiswa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer jurusan_id
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<MahasiswaDTO> mahasiswaPage = mahasiswaService.getAllMahasiswa(pageable, search, jurusan_id);
        return ResponseEntity.ok(mahasiswaPage);
    }

    /**
     * Endpoint untuk melihat detail satu mahasiswa berdasarkan ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MahasiswaDTO> getMahasiswaById(@PathVariable Integer id) {
        MahasiswaDTO mahasiswa = mahasiswaService.getMahasiswaById(id);
        return ResponseEntity.ok(mahasiswa);
    }

    /**
     * Endpoint untuk mengubah biodata mahasiswa.
     */
    @PutMapping("/{id}/biodata")
    public ResponseEntity<String> updateBiodata(@PathVariable Integer id, @Valid @RequestBody BiodataUpdateDTO biodataDTO) {
        mahasiswaService.updateBiodata(id, biodataDTO);
        return ResponseEntity.ok("Biodata mahasiswa berhasil diperbarui.");
    }
}