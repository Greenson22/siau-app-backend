package com.sttis.services;

import com.sttis.dto.DashboardSummaryDTO;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.PembayaranRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // Di sini kita asumsikan "Pendaftar Baru" adalah mahasiswa yang status pembayarannya masih PENDING.
        // Ini adalah metrik yang paling mendekati deskripsi Anda.
        long pembayaranPending = pembayaranRepository.countByStatusVerifikasi(StatusVerifikasi.PENDING);
        dto.setPendaftarBaru(pembayaranPending); // Menggunakan data pembayaran pending sebagai pendaftar baru
        dto.setPembayaranMenungguVerifikasi(pembayaranPending);

        dto.setTotalMahasiswaAktif(mahasiswaRepository.countByStatus(StatusMahasiswa.AKTIF));
        dto.setTotalDosen(dosenRepository.count());

        return dto;
    }
}