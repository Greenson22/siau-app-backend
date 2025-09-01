// java-spring-boot/com/sttis/controllers/FakultasController.java
package com.sttis.controllers;

import com.sttis.dto.FakultasDTO;
import com.sttis.services.FakultasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fakultas")
public class FakultasController {

    private final FakultasService fakultasService;

    public FakultasController(FakultasService fakultasService) {
        this.fakultasService = fakultasService;
    }

    @GetMapping
    public ResponseEntity<List<FakultasDTO>> getAllFakultas() {
        return ResponseEntity.ok(fakultasService.getAllFakultas());
    }

    @PostMapping
    public ResponseEntity<FakultasDTO> createFakultas(@Valid @RequestBody FakultasDTO fakultasDTO) {
        FakultasDTO createdFakultas = fakultasService.createFakultas(fakultasDTO);
        return new ResponseEntity<>(createdFakultas, HttpStatus.CREATED);
    }
}