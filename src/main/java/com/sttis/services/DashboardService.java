// program/java-spring-boot/com/sttis/services/DashboardService.java
package com.sttis.services;

import com.sttis.dto.DashboardSummaryDTO;
import com.sttis.dto.JurusanPendaftarDTO; // <-- IMPORT BARU
import com.sttis.dto.PendaftaranChartDTO; // <-- IMPORT BARU
import com.sttis.dto.TrenPendaftaranDTO; // <-- IMPORT BARU
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.PembayaranRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; // <-- IMPORT BARU
import java.util.List;      // <-- IMPORT BARU

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

    /**
     * METHOD BARU: Mengambil data untuk chart pendaftaran.
     */
    public PendaftaranChartDTO getPendaftaranChartData() {
        PendaftaranChartDTO chartDTO = new PendaftaranChartDTO();

        // 1. Ambil data sebaran jurusan
        List<JurusanPendaftarDTO> sebaranJurusan = mahasiswaRepository.findSebaranJurusanPendaftar();
        chartDTO.setSebaranJurusan(sebaranJurusan);

        // 2. Data tren pendaftaran (masih menggunakan data dummy karena data tanggal registrasi tidak ada)
        // Jika ada kolom `tanggalRegistrasi` di entitas Mahasiswa, query bisa dibuat lebih kompleks.
        List<TrenPendaftaranDTO> tren = new ArrayList<>();
        tren.add(new TrenPendaftaranDTO("2025-01", 15L));
        tren.add(new TrenPendaftaranDTO("2025-02", 25L));
        tren.add(new TrenPendaftaranDTO("2025-03", 20L));
        chartDTO.setTrenPendaftaran(tren);

        return chartDTO;
    }
}