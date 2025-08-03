package com.sttis.services;

import com.sttis.dto.KrsDTO;
import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.KelasRepository;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KrsService {

    private final KrsRepository krsRepository;
    private final UserRepository userRepository;
    private final KelasRepository kelasRepository;

    public KrsService(KrsRepository krsRepository, UserRepository userRepository, KelasRepository kelasRepository) {
        this.krsRepository = krsRepository;
        this.userRepository = userRepository;
        this.kelasRepository = kelasRepository;
    }

    /**
     * Mengambil daftar KRS milik mahasiswa yang sedang login.
     */
    public List<KrsDTO> getMyKrs(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Mahasiswa mahasiswa = user.getMahasiswa();
        if (mahasiswa == null) {
            throw new RuntimeException("User ini bukan mahasiswa.");
        }

        return krsRepository.findByMahasiswa(mahasiswa)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Menambahkan kelas baru ke KRS mahasiswa.
     */
    public KrsDTO addKelasToMyKrs(Integer kelasId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Mahasiswa mahasiswa = user.getMahasiswa();

        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas dengan ID " + kelasId + " tidak ditemukan."));

        // Validasi: Cek apakah mahasiswa sudah mengambil kelas ini sebelumnya
        if (krsRepository.existsByMahasiswaAndKelas(mahasiswa, kelas)) {
            throw new IllegalStateException("Anda sudah mengambil mata kuliah ini.");
        }
        
        Krs newKrs = new Krs();
        newKrs.setMahasiswa(mahasiswa);
        newKrs.setKelas(kelas);
        newKrs.setStatusPersetujuan(StatusPersetujuan.DIAJUKAN); // Status default

        Krs savedKrs = krsRepository.save(newKrs);
        return convertToDTO(savedKrs);
    }
    
    /**
     * Menghapus kelas dari KRS, hanya jika statusnya masih "DIAJUKAN".
     */
    public void deleteKrs(Integer krsId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Mahasiswa mahasiswa = user.getMahasiswa();

        Krs krs = krsRepository.findById(krsId)
                .orElseThrow(() -> new RuntimeException("KRS dengan ID " + krsId + " tidak ditemukan."));

        // Validasi: Pastikan mahasiswa hanya bisa menghapus KRS miliknya sendiri
        if (!krs.getMahasiswa().equals(mahasiswa)) {
            throw new SecurityException("Anda tidak berhak menghapus KRS ini.");
        }
        
        // Validasi: Pastikan KRS hanya bisa dihapus jika belum disetujui
        if (krs.getStatusPersetujuan() != StatusPersetujuan.DIAJUKAN) {
            throw new IllegalStateException("KRS tidak dapat dihapus karena sudah disetujui atau ditolak.");
        }

        krsRepository.delete(krs);
    }

    private KrsDTO convertToDTO(Krs krs) {
        KrsDTO dto = new KrsDTO();
        Kelas kelas = krs.getKelas();
        dto.setKrsId(krs.getKrsId());
        dto.setKelasId(kelas.getKelasId());
        dto.setKodeMataKuliah(kelas.getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(kelas.getMataKuliah().getNamaMatkul());
        dto.setSks(kelas.getMataKuliah().getSks());
        dto.setNamaDosen(kelas.getDosen().getNamaLengkap());
        dto.setStatusPersetujuan(krs.getStatusPersetujuan().name());
        dto.setJadwal(kelas.getHari() + ", " + kelas.getJamMulai());
        return dto;
    }
}