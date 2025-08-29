package com.sttis.controllers;

import com.sttis.dto.PaketKrsInputDTO;
import com.sttis.dto.PaketMatakuliahDTO;
import com.sttis.services.PaketKrsAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paket-krs")
public class PaketKrsController {

    private final PaketKrsAdminService paketKrsAdminService;

    public PaketKrsController(PaketKrsAdminService paketKrsAdminService) {
        this.paketKrsAdminService = paketKrsAdminService;
    }

    @GetMapping
    public ResponseEntity<List<PaketMatakuliahDTO>> getAllPaketKrs() {
        return ResponseEntity.ok(paketKrsAdminService.getAllPaket());
    }

    @PostMapping
    public ResponseEntity<PaketMatakuliahDTO> createPaketKrs(@Valid @RequestBody PaketKrsInputDTO input) {
        PaketMatakuliahDTO createdPaket = paketKrsAdminService.createPaket(input);
        return new ResponseEntity<>(createdPaket, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaketMatakuliahDTO> updatePaketKrs(@PathVariable Integer id, @Valid @RequestBody PaketKrsInputDTO input) {
        PaketMatakuliahDTO updatedPaket = paketKrsAdminService.updatePaket(id, input);
        return ResponseEntity.ok(updatedPaket);
    }
}
