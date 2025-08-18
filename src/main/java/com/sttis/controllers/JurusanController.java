// program/java-spring-boot/com/sttis/controllers/JurusanController.java

package com.sttis.controllers;

import com.sttis.dto.JurusanDTO;
import com.sttis.services.JurusanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jurusan")
public class JurusanController {

    private final JurusanService jurusanService;

    public JurusanController(JurusanService jurusanService) {
        this.jurusanService = jurusanService;
    }

    /**
     * Endpoint untuk melihat daftar semua jurusan.
     * Method: GET
     * URL: /api/jurusan
     */
    @GetMapping
    public ResponseEntity<List<JurusanDTO>> getAllJurusan() {
        List<JurusanDTO> jurusanList = jurusanService.getAllJurusan();
        return ResponseEntity.ok(jurusanList);
    }

    /**
     * Endpoint untuk menambah jurusan baru.
     * Method: POST
     * URL: /api/jurusan
     */
    @PostMapping
    public ResponseEntity<JurusanDTO> createJurusan(@Valid @RequestBody JurusanDTO jurusanDTO) {
        JurusanDTO createdJurusan = jurusanService.createJurusan(jurusanDTO);
        return new ResponseEntity<>(createdJurusan, HttpStatus.CREATED);
    }

    /**
     * Endpoint untuk mengubah data jurusan yang sudah ada.
     * Method: PUT
     * URL: /api/jurusan/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<JurusanDTO> updateJurusan(@PathVariable Integer id, @Valid @RequestBody JurusanDTO jurusanDTO) {
        JurusanDTO updatedJurusan = jurusanService.updateJurusan(id, jurusanDTO);
        return ResponseEntity.ok(updatedJurusan);
    }
}