package com.sttis.services;

import com.sttis.dto.KrsDTO;
import com.sttis.dto.MahasiswaDTO;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DosenPaService {

    private final UserRepository userRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final KrsRepository krsRepository;

    public DosenPaService(UserRepository userRepository, MahasiswaRepository mahasiswaRepository, KrsRepository krsRepository) {
        this.userRepository = userRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.krsRepository = krsRepository;
    }

    /**
     * Mengambil daftar mahasiswa yang menjadi bimbingan Dosen PA.
     */
    public List<MahasiswaDTO> getMahasiswaBimbingan(String username) {
        Dosen dosenPa = getDosenFromUsername(username);
        return dosenPa.getMahasiswaBimbingan().stream()
                .map(this::convertToMahasiswaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Melihat KRS milik mahasiswa bimbingan.
     */
    public List<KrsDTO> getKrsMahasiswaBimbingan(Integer mahasiswaId, String username) {
        Dosen dosenPa = getDosenFromUsername(username);
        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                .orElseThrow(() -> new RuntimeException("Mahasiswa dengan ID " + mahasiswaId + " tidak ditemukan."));

        // Validasi: Pastikan mahasiswa tersebut adalah bimbingan Dosen PA yang login
        if (mahasiswa.getPembimbingAkademik() == null || !mahasiswa.getPembimbingAkademik().equals(dosenPa)) {
            throw new SecurityException("Anda bukan Dosen PA dari mahasiswa ini.");
        }

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                .map(this::convertToKrsDTO)
                .collect(Collectors.toList());
    }
    
    // Helper Methods
    private Dosen getDosenFromUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }
        return dosen;
    }

    private MahasiswaDTO convertToMahasiswaDTO(Mahasiswa mahasiswa) {
        MahasiswaDTO dto = new MahasiswaDTO();
        dto.setMahasiswaId(mahasiswa.getMahasiswaId());
        dto.setNim(mahasiswa.getNim());
        dto.setNamaLengkap(mahasiswa.getNamaLengkap());
        dto.setStatus(mahasiswa.getStatus().name());
        if (mahasiswa.getJurusan() != null) {
            dto.setNamaJurusan(mahasiswa.getJurusan().getNamaJurusan());
        }
        return dto;
    }

    private KrsDTO convertToKrsDTO(Krs krs) {
        KrsDTO dto = new KrsDTO();
        dto.setKrsId(krs.getKrsId());
        dto.setKelasId(krs.getKelas().getKelasId());
        dto.setKodeMataKuliah(krs.getKelas().getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(krs.getKelas().getMataKuliah().getNamaMatkul());
        dto.setSks(krs.getKelas().getMataKuliah().getSks());
        dto.setStatusPersetujuan(krs.getStatusPersetujuan().name());
        return dto;
    }
}