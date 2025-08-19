package com.sttis.services;

import com.sttis.dto.ActionItemDTO;
import com.sttis.dto.DashboardSummaryDTO;
import com.sttis.dto.JurusanPendaftarDTO;
import com.sttis.dto.PendaftaranChartDTO;
import com.sttis.dto.PendingPembayaranDTO;
import com.sttis.dto.TrenPendaftaranDTO;
import com.sttis.models.entities.Pembayaran;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.PembayaranRepository;
import com.sttis.models.repos.UserRepository;
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
    private final UserRepository userRepository;


    public DashboardService(MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository,
                            PembayaranRepository pembayaranRepository, UserRepository userRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
        this.pembayaranRepository = pembayaranRepository;
        this.userRepository = userRepository;
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

        // 2. Logika untuk pendaftar baru dan notifikasi lain bisa ditambahkan di sini

        return actionItemDTO;
    }

    /**
     * BARU: Helper untuk konversi Pembayaran ke PendingPembayaranDTO.
     */
    private PendingPembayaranDTO convertToPendingPembayaranDTO(Pembayaran pembayaran) {
        PendingPembayaranDTO dto = new PendingPembayaranDTO();
        dto.setPembayaranId(pembayaran.getPembayaranId());
        dto.setNamaMahasiswa(pembayaran.getTagihan().getMahasiswa().getNamaLengkap());
        dto.setDeskripsiTagihan(pembayaran.getTagihan().getDeskripsiTagihan());
        dto.setJumlahBayar(pembayaran.getJumlahBayar());
        dto.setTanggalBayar(pembayaran.getTanggalBayar());
        return dto;
    }
}