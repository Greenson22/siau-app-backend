package com.sttis.services;

import com.sttis.dto.DetailPaketDTO;
import com.sttis.dto.PaketMatakuliahDTO;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.PaketMatakuliah;
import com.sttis.models.entities.User;
import com.sttis.models.repos.PaketMatakuliahRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaketMatakuliahService {

    private final PaketMatakuliahRepository paketMatakuliahRepository;
    private final UserRepository userRepository;
    private final AkademikService akademikService; // Menggunakan service yang ada

    public PaketMatakuliahService(PaketMatakuliahRepository paketMatakuliahRepository, UserRepository userRepository, AkademikService akademikService) {
        this.paketMatakuliahRepository = paketMatakuliahRepository;
        this.userRepository = userRepository;
        this.akademikService = akademikService;
    }

    /**
     * Mengambil paket mata kuliah yang disarankan untuk mahasiswa yang sedang login
     * berdasarkan jurusan dan semester aktif mereka.
     */
    public PaketMatakuliahDTO getPaketForCurrentMahasiswa(String username) {
        // 1. Dapatkan data mahasiswa
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Mahasiswa mahasiswa = user.getMahasiswa();
        if (mahasiswa == null) {
            throw new IllegalStateException("User ini bukan mahasiswa.");
        }
        Jurusan jurusan = mahasiswa.getJurusan();

        // 2. Dapatkan semester aktif mahasiswa
        Integer semesterAktif = akademikService.getAkademikSummary(username).getSemesterAktif();

        // 3. Cari paket matakuliah di database
        // Anggap saja tahun akademik diambil yang paling baru, bisa disesuaikan
        PaketMatakuliah paket = paketMatakuliahRepository.findByJurusanAndSemester(jurusan, semesterAktif)
                .orElseThrow(() -> new RuntimeException("Paket matakuliah untuk semester " + semesterAktif + " tidak ditemukan."));

        // 4. Konversi ke DTO
        return convertToDto(paket);
    }

    private PaketMatakuliahDTO convertToDto(PaketMatakuliah paket) {
        PaketMatakuliahDTO dto = new PaketMatakuliahDTO();
        dto.setPaketId(paket.getPaketId());
        dto.setNamaPaket(paket.getNamaPaket());
        dto.setSemester(paket.getSemester());
        if (paket.getJurusan() != null) {
            dto.setNamaJurusan(paket.getJurusan().getNamaJurusan());
        }

        dto.setDetailPaket(paket.getDetailPaket().stream().map(detail -> {
            DetailPaketDTO detailDto = new DetailPaketDTO();
            detailDto.setMatkulId(detail.getMataKuliah().getMatkulId());
            detailDto.setKodeMatkul(detail.getMataKuliah().getKodeMatkul());
            detailDto.setNamaMatkul(detail.getMataKuliah().getNamaMatkul());
            return detailDto;
        }).collect(Collectors.toList()));

        int totalSks = paket.getDetailPaket().stream()
                .mapToInt(d -> d.getMataKuliah().getSks())
                .sum();
        dto.setTotalSks(totalSks);

        return dto;
    }
}
