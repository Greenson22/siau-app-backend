package com.sttis.services;

import com.sttis.dto.ActivityLogDTO;
import com.sttis.dto.PengumumanDTO;
import com.sttis.dto.PengumumanInputDTO;
import com.sttis.models.entities.ActivityLog;
import com.sttis.models.entities.Pengumuman;
import com.sttis.models.entities.User;
import com.sttis.models.repos.ActivityLogRepository;
import com.sttis.models.repos.PengumumanRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SystemService {

    private final PengumumanRepository pengumumanRepo;
    private final ActivityLogRepository activityLogRepo;
    private final UserRepository userRepo;

    public SystemService(PengumumanRepository pengumumanRepo, ActivityLogRepository activityLogRepo, UserRepository userRepo) {
        this.pengumumanRepo = pengumumanRepo;
        this.activityLogRepo = activityLogRepo;
        this.userRepo = userRepo;
    }

    // --- Logika Pengumuman ---
    @Transactional(readOnly = true)
    public List<PengumumanDTO> getAllPengumuman() {
        return pengumumanRepo.findAll(Sort.by(Sort.Direction.DESC, "tanggalTerbit")).stream()
                .map(this::convertToPengumumanDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PengumumanDTO getPengumumanById(Integer id) {
        return pengumumanRepo.findById(id)
                .map(this::convertToPengumumanDTO)
                .orElseThrow(() -> new RuntimeException("Pengumuman dengan ID " + id + " tidak ditemukan."));
    }

    public PengumumanDTO createPengumuman(PengumumanInputDTO input, String username) {
        User pembuat = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan."));

        Pengumuman pengumuman = new Pengumuman();
        pengumuman.setJudul(input.getJudul());
        pengumuman.setIsi(input.getIsi());
        pengumuman.setPembuat(pembuat);
        pengumuman.setTanggalTerbit(LocalDateTime.now());

        Pengumuman savedPengumuman = pengumumanRepo.save(pengumuman);
        return convertToPengumumanDTO(savedPengumuman);
    }

    // --- Logika Activity Log ---
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivityLogs() {
        return activityLogRepo.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).stream()
                .map(this::convertToActivityLogDTO)
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---
    private PengumumanDTO convertToPengumumanDTO(Pengumuman pengumuman) {
        PengumumanDTO dto = new PengumumanDTO();
        dto.setPengumumanId(pengumuman.getPengumumanId());
        dto.setJudul(pengumuman.getJudul());
        dto.setIsi(pengumuman.getIsi());
        dto.setTanggalTerbit(pengumuman.getTanggalTerbit());
        // Menampilkan nama pembuat berdasarkan role
        if (pengumuman.getPembuat().getRole().getRoleName().equals("Dosen")) {
            dto.setNamaPembuat(pengumuman.getPembuat().getDosen().getNamaLengkap());
        } else {
            dto.setNamaPembuat(pengumuman.getPembuat().getRole().getRoleName()); // "Admin"
        }
        return dto;
    }

    private ActivityLogDTO convertToActivityLogDTO(ActivityLog log) {
        ActivityLogDTO dto = new ActivityLogDTO();
        dto.setLogId(log.getLogId());
        if (log.getUser() != null) {
            dto.setUsername(log.getUser().getUsername());
        }
        dto.setAksi(log.getAksi());
        dto.setDeskripsi(log.getDeskripsi());
        dto.setIpAddress(log.getIpAddress());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}