package com.sttis.services;

import com.sttis.dto.NilaiInputDTO;
import com.sttis.dto.PersetujuanDTO;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.User;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KrsManagementService {

    private final KrsRepository krsRepository;
    private final UserRepository userRepository;

    public KrsManagementService(KrsRepository krsRepository, UserRepository userRepository) {
        this.krsRepository = krsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Mengupdate nilai akhir dan nilai huruf untuk sebuah KRS.
     * Hanya bisa dilakukan oleh dosen pengajar kelas tersebut.
     */
    public Krs updateNilai(Integer krsId, NilaiInputDTO nilaiInput, String username) {
        Dosen dosen = getDosenFromUsername(username);
        Krs krs = krsRepository.findById(krsId)
                .orElseThrow(() -> new RuntimeException("KRS dengan ID " + krsId + " tidak ditemukan."));

        // Validasi: Pastikan dosen yang login adalah pengajar di kelas tersebut
        if (!krs.getKelas().getDosen().equals(dosen)) {
            throw new SecurityException("Anda bukan pengajar di kelas ini.");
        }

        krs.setNilaiAkhir(nilaiInput.getNilaiAkhir());
        krs.setNilaiHuruf(nilaiInput.getNilaiHuruf());
        return krsRepository.save(krs);
    }

    /**
     * Mengupdate status persetujuan KRS (Disetujui/Ditolak).
     * Hanya bisa dilakukan oleh Dosen Pembimbing Akademik (PA) mahasiswa.
     */
    public Krs updateStatusPersetujuan(Integer krsId, PersetujuanDTO persetujuanInput, String username) {
        Dosen dosenPa = getDosenFromUsername(username);
        Krs krs = krsRepository.findById(krsId)
                .orElseThrow(() -> new RuntimeException("KRS dengan ID " + krsId + " tidak ditemukan."));

        // Validasi: Pastikan dosen yang login adalah Dosen PA dari mahasiswa pemilik KRS
        if (!krs.getMahasiswa().getPembimbingAkademik().equals(dosenPa)) {
            throw new SecurityException("Anda bukan Dosen PA dari mahasiswa ini.");
        }

        krs.setStatusPersetujuan(StatusPersetujuan.valueOf(persetujuanInput.getStatusPersetujuan().toUpperCase()));
        return krsRepository.save(krs);
    }

    private Dosen getDosenFromUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }
        return dosen;
    }
}