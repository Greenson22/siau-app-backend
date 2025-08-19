// program/java-spring-boot/com/sttis/services/DashboardService.java
package com.sttis.services;

import com.sttis.dto.*;
import com.sttis.models.entities.ActivityLog;
import com.sttis.models.entities.Pembayaran;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.ActivityLogRepository;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.PembayaranRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;
    private final PembayaranRepository pembayaranRepository;
    private final ActivityLogRepository activityLogRepository; // <-- DEPENDENCY BARU


    public DashboardService(MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository,
                            PembayaranRepository pembayaranRepository, ActivityLogRepository activityLogRepository) { // <-- INJEKSI BARU
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
        this.pembayaranRepository = pembayaranRepository;
        this.activityLogRepository = activityLogRepository; // <-- SET DEPENDENCY
    }

    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        long pembayaranPending = pembayaranRepository.countByStatusVerifikasi(StatusVerifikasi.PENDING);
        dto.setPendaftarBaru(pembayaranPending);
        dto.setPembayaranMenungguVerifikasi(pembayaranPending);
        dto.setTotalMahasiswaAktif(mahasiswaRepository.countByStatus(StatusMahasiswa.AKTIF));
        dto.setTotalDosen(dosenRepository.count());
        return dto;
    }
    
    public PendaftaranChartDTO getPendaftaranChartData() {
        PendaftaranChartDTO chartDTO = new PendaftaranChartDTO();

        List<JurusanPendaftarDTO> sebaranJurusan = mahasiswaRepository.findSebaranJurusanPendaftar();
        chartDTO.setSebaranJurusan(sebaranJurusan);
        
        List<TrenPendaftaranDTO> tren = new ArrayList<>();
        tren.add(new TrenPendaftaranDTO("2025-01", 15L));
        tren.add(new TrenPendaftaranDTO("2025-02", 25L));
        tren.add(new TrenPendaftaranDTO("2025-03", 20L));
        chartDTO.setTrenPendaftaran(tren);

        return chartDTO;
    }

    /**
     * BARU: Mengambil data untuk daftar tugas & notifikasi mendesak.
     */
    public ActionItemDTO getDashboardActionItems() {
        ActionItemDTO actionItemDTO = new ActionItemDTO();

        // 1. Ambil pembayaran yang menunggu verifikasi
        List<PendingPembayaranDTO> pendingPembayaran = pembayaranRepository.findByStatusVerifikasi(StatusVerifikasi.PENDING)
                .stream()
                .map(this::convertToPendingPembayaranDTO)
                .collect(Collectors.toList());
        actionItemDTO.setPembayaranMenungguVerifikasi(pendingPembayaran);

        // 2. Ambil 5 log aktivitas terbaru
        List<ActivityLogDTO> latestLogs = activityLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).stream()
                .limit(5)
                .map(this::convertToActivityLogDTO)
                .collect(Collectors.toList());
        actionItemDTO.setLatestActivities(latestLogs);

        return actionItemDTO;
    }
    
    // ... (Helper method lainnya) ...

    private PendingPembayaranDTO convertToPendingPembayaranDTO(Pembayaran pembayaran) {
        PendingPembayaranDTO dto = new PendingPembayaranDTO();
        dto.setPembayaranId(pembayaran.getPembayaranId());
        dto.setNamaMahasiswa(pembayaran.getTagihan().getMahasiswa().getNamaLengkap());
        dto.setDeskripsiTagihan(pembayaran.getTagihan().getDeskripsiTagihan());
        dto.setJumlahBayar(pembayaran.getJumlahBayar());
        dto.setTanggalBayar(pembayaran.getTanggalBayar());
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