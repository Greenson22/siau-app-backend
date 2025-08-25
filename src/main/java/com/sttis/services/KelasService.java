// program/java-spring-boot/services/KelasService.java
package com.sttis.services;

import com.sttis.dto.MahasiswaDiKelasDTO;
import com.sttis.dto.PresensiInputDTO;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.StatusHadir;
import com.sttis.models.repos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KelasService {

    private final KelasRepository kelasRepository;
    private final KrsRepository krsRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final PresensiMahasiswaRepository presensiMahasiswaRepository;
    private final UserRepository userRepository;

    public KelasService(KelasRepository kelasRepository, KrsRepository krsRepository, MahasiswaRepository mahasiswaRepository, PresensiMahasiswaRepository presensiMahasiswaRepository, UserRepository userRepository) {
        this.kelasRepository = kelasRepository;
        this.krsRepository = krsRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.presensiMahasiswaRepository = presensiMahasiswaRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<MahasiswaDiKelasDTO> getMahasiswaDiKelas(Integer kelasId, String username) {
        Kelas kelas = getAndVerifyDosenKelas(kelasId, username);

        return krsRepository.findByKelas(kelas).stream()
                .map(krs -> {
                    MahasiswaDiKelasDTO dto = new MahasiswaDiKelasDTO();
                    dto.setKrsId(krs.getKrsId()); // <-- LOGIKA BARU
                    dto.setMahasiswaId(krs.getMahasiswa().getMahasiswaId());
                    dto.setNim(krs.getMahasiswa().getNim());
                    dto.setNamaLengkap(krs.getMahasiswa().getNamaLengkap());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    // ... (sisa kode tidak berubah)
    public void isiPresensi(Integer kelasId, PresensiInputDTO input, String username) {
        Kelas kelas = getAndVerifyDosenKelas(kelasId, username);

        for (PresensiInputDTO.StatusPresensiDTO statusMhs : input.getStatusPresensi()) {
            Mahasiswa mahasiswa = mahasiswaRepository.findById(statusMhs.getMahasiswaId())
                    .orElseThrow(() -> new RuntimeException("Mahasiswa dengan ID " + statusMhs.getMahasiswaId() + " tidak ditemukan."));

            Krs krs = krsRepository.findByMahasiswaAndKelas(mahasiswa, kelas)
                    .orElseThrow(() -> new RuntimeException("Mahasiswa " + mahasiswa.getNamaLengkap() + " tidak terdaftar di kelas ini."));

            if (presensiMahasiswaRepository.existsByKrsAndPertemuanKe(krs, input.getPertemuanKe())) {
                throw new IllegalStateException("Presensi untuk " + mahasiswa.getNamaLengkap() + " di pertemuan ke-" + input.getPertemuanKe() + " sudah ada.");
            }

            PresensiMahasiswa presensi = new PresensiMahasiswa();
            presensi.setKrs(krs);
            presensi.setPertemuanKe(input.getPertemuanKe());
            presensi.setTanggal(LocalDate.now());
            presensi.setStatusHadir(StatusHadir.valueOf(statusMhs.getStatusHadir().toUpperCase()));

            presensiMahasiswaRepository.save(presensi);
        }
    }

    private Kelas getAndVerifyDosenKelas(Integer kelasId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }

        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas dengan ID " + kelasId + " tidak ditemukan."));

        if (!kelas.getDosen().equals(dosen)) {
            throw new SecurityException("Anda tidak memiliki akses ke kelas ini.");
        }
        return kelas;
    }
}