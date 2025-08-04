package com.sttis.services;

import com.sttis.dto.DosenBiodataUpdateDTO;
import com.sttis.dto.DosenDTO;
import com.sttis.dto.KelasDTO;
import com.sttis.models.entities.BiodataDosen;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.User;
import com.sttis.models.repos.BiodataDosenRepository;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.KelasRepository;
import com.sttis.models.repos.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // Pindahkan transactional ke level class
public class DosenService {

    private final DosenRepository dosenRepository;
    private final BiodataDosenRepository biodataDosenRepository;
    private final UserRepository userRepository; // <-- Tambah dependensi
    private final KelasRepository kelasRepository; // <-- Tambah dependensi

    // Perbarui Constructor
    public DosenService(DosenRepository dosenRepository, BiodataDosenRepository biodataDosenRepository, UserRepository userRepository, KelasRepository kelasRepository) {
        this.dosenRepository = dosenRepository;
        this.biodataDosenRepository = biodataDosenRepository;
        this.userRepository = userRepository;
        this.kelasRepository = kelasRepository;
    }

    public List<DosenDTO> getAllDosen() {
        return dosenRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DosenDTO getDosenById(Integer id) {
        Dosen dosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen dengan ID " + id + " tidak ditemukan."));
        return convertToDTO(dosen);
    }

    public BiodataDosen updateBiodata(Integer dosenId, DosenBiodataUpdateDTO biodataDTO) {
        Dosen dosen = dosenRepository.findById(dosenId)
                .orElseThrow(() -> new RuntimeException("Dosen dengan ID " + dosenId + " tidak ditemukan."));

        BiodataDosen biodata = dosen.getBiodata();
        if (biodata == null) {
            biodata = new BiodataDosen();
            biodata.setDosen(dosen);
        }

        biodata.setAlamat(biodataDTO.getAlamat());
        biodata.setNomorTelepon(biodataDTO.getNomorTelepon());
        biodata.setEmailPribadi(biodataDTO.getEmailPribadi());
        biodata.setSpesialisasi(biodataDTO.getSpesialisasi());

        return biodataDosenRepository.save(biodata);
    }

        /**
     * BARU: Mengambil daftar kelas yang diampu oleh dosen yang login.
     */
    @Transactional(readOnly = true)
    public List<KelasDTO> getKelasDiampu(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }
        
        List<Kelas> kelasList = kelasRepository.findByDosen(dosen);
        return kelasList.stream()
                .map(this::convertToKelasDTO)
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private DosenDTO convertToDTO(Dosen dosen) {
        // ... (Tidak ada perubahan pada metode ini)
        DosenDTO dto = new DosenDTO();
        dto.setDosenId(dosen.getDosenId());
        dto.setNidn(dosen.getNidn());
        dto.setNamaLengkap(dosen.getNamaLengkap());

        if (dosen.getJurusan() != null) {
            dto.setNamaJurusan(dosen.getJurusan().getNamaJurusan());
        }
        if (dosen.getBiodata() != null) {
            dto.setSpesialisasi(dosen.getBiodata().getSpesialisasi());
        }
        return dto;
    }
    
    /**
     * BARU: Helper method untuk mengubah entitas Kelas menjadi KelasDTO.
     */
    private KelasDTO convertToKelasDTO(Kelas kelas) {
        KelasDTO dto = new KelasDTO();
        dto.setKelasId(kelas.getKelasId());
        dto.setKodeMataKuliah(kelas.getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(kelas.getMataKuliah().getNamaMatkul());
        dto.setSks(kelas.getMataKuliah().getSks());
        dto.setDosenPengajar(kelas.getDosen().getNamaLengkap());
        dto.setJadwal(kelas.getHari() + ", " + kelas.getJamMulai());
        dto.setRuangan(kelas.getRuangan());
        return dto;
    }
}