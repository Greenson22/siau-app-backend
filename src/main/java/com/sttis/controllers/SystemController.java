package com.sttis.controllers;

import com.sttis.dto.ActivityLogDTO;
import com.sttis.dto.PengumumanDTO;
import com.sttis.dto.PengumumanInputDTO;
import com.sttis.services.SystemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    // == PENGUMUMAN ==
    @GetMapping("/pengumuman")
    public ResponseEntity<List<PengumumanDTO>> getAllPengumuman() {
        return ResponseEntity.ok(systemService.getAllPengumuman());
    }

    @GetMapping("/pengumuman/{id}")
    public ResponseEntity<PengumumanDTO> getPengumumanById(@PathVariable Integer id) {
        return ResponseEntity.ok(systemService.getPengumumanById(id));
    }

    @PostMapping("/pengumuman")
    public ResponseEntity<PengumumanDTO> createPengumuman(@Valid @RequestBody PengumumanInputDTO input, Authentication authentication) {
        PengumumanDTO created = systemService.createPengumuman(input, authentication.getName());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // == ACTIVITY LOG ==
    @GetMapping("/logs/activity")
    public ResponseEntity<List<ActivityLogDTO>> getActivityLogs() {
        return ResponseEntity.ok(systemService.getActivityLogs());
    }
}