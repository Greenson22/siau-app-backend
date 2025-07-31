package com.sttis.controllers;

import com.sttis.dto.MahasiswaDTO;
import com.sttis.services.MahasiswaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}