package com.sttis.services;

import com.sttis.dto.DetailPresensiDTO;
import com.sttis.dto.KhsDTO;
import com.sttis.dto.RekapPresensiDTO;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Gunakan readOnly untuk operasi GET
public class AkademikService {

    private final UserRepository userRepository;
    private final KrsRepository krsRepository;

    public AkademikService(UserRepository userRepository, KrsRepository krsRepository) {
        this.userRepository = userRepository;
        this.krsRepository = krsRepository;
    }

    /**
     * Mengambil Kartu Hasil Studi (KHS) untuk mahasiswa yang sedang login.
     * Hanya menampilkan mata kuliah yang sudah memiliki nilai.
     */
    public List<KhsDTO> getMyKhs(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                .filter(krs -> krs.getNilaiHuruf() != null && !krs.getNilaiHuruf().isBlank())
                .map(this::convertToKhsDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Mengambil rekapitulasi presensi per kelas untuk mahasiswa yang sedang login.
     */
    public List<RekapPresensiDTO> getMyRekapPresensi(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                .map(this::convertToRekapPresensiDTO)
                .collect(Collectors.toList());
    }

    // Helper untuk mencari mahasiswa berdasarkan username
    private Mahasiswa getMahasiswaFromUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        if (user.getMahasiswa() == null) {
            throw new IllegalStateException("User ini bukan seorang mahasiswa.");
        }
        return user.getMahasiswa();
    }

    // Helper untuk konversi ke DTO
    private KhsDTO convertToKhsDTO(Krs krs) {
        KhsDTO dto = new KhsDTO();
        dto.setKodeMataKuliah(krs.getKelas().getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(krs.getKelas().getMataKuliah().getNamaMatkul());
        dto.setSks(krs.getKelas().getMataKuliah().getSks());
        dto.setTahunAkademik(krs.getKelas().getTahunAkademik());
        dto.setSemester(krs.getKelas().getSemester().name());
        dto.setNilaiAkhir(krs.getNilaiAkhir());
        dto.setNilaiHuruf(krs.getNilaiHuruf());
        return dto;
    }

    private RekapPresensiDTO convertToRekapPresensiDTO(Krs krs) {
        RekapPresensiDTO dto = new RekapPresensiDTO();
        dto.setKodeMataKuliah(krs.getKelas().getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(krs.getKelas().getMataKuliah().getNamaMatkul());
        dto.setNamaDosen(krs.getKelas().getDosen().getNamaLengkap());

        List<DetailPresensiDTO> detailList = krs.getPresensiList().stream().map(presensi -> {
            DetailPresensiDTO detail = new DetailPresensiDTO();
            detail.setPertemuanKe(presensi.getPertemuanKe());
            detail.setTanggal(presensi.getTanggal());
            detail.setStatusHadir(presensi.getStatusHadir().name());
            return detail;
        }).collect(Collectors.toList());
        
        dto.setDetailPresensi(detailList);
        return dto;
    }
}