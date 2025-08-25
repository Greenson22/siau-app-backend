// program/java-spring-boot/services/DosenService.java

package com.sttis.services;

import com.sttis.dto.DosenBiodataUpdateDTO;
import com.sttis.dto.DosenDTO;
import com.sttis.dto.DosenDashboardSummaryDTO;
import com.sttis.dto.DosenProfileDTO; // <-- IMPORT BARU
import com.sttis.dto.KelasDTO;
import com.sttis.models.entities.BiodataDosen;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.User;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.BiodataDosenRepository;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.KelasRepository;
import com.sttis.models.repos.UserRepository;

import com.sttis.models.repos.specifications.DosenSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DosenService {

    private final DosenRepository dosenRepository;
    private final BiodataDosenRepository biodataDosenRepository;
    private final UserRepository userRepository; 
    private final KelasRepository kelasRepository; 

    public DosenService(DosenRepository dosenRepository, BiodataDosenRepository biodataDosenRepository, UserRepository userRepository, KelasRepository kelasRepository) {
        this.dosenRepository = dosenRepository;
        this.biodataDosenRepository = biodataDosenRepository;
        this.userRepository = userRepository;
        this.kelasRepository = kelasRepository;
    }

    @Transactional(readOnly = true)
    public Page<DosenDTO> getAllDosen(Pageable pageable, String search, Integer jurusanId) {
        var spec = DosenSpecification.findByCriteria(search, jurusanId);
        Page<Dosen> dosenPage = dosenRepository.findAll(spec, pageable);
        return dosenPage.map(this::convertToDTO);
    }

    public DosenDTO getDosenById(Integer id) {
        Dosen dosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen dengan ID " + id + " tidak ditemukan."));
        return convertToDTO(dosen);
    }
    
    /**
     * BARU: Mengambil data profil lengkap untuk dosen yang sedang login.
     */
    @Transactional(readOnly = true)
    public DosenProfileDTO getDosenProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }
        
        return convertToProfileDTO(dosen);
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

    @Transactional(readOnly = true)
    public DosenDashboardSummaryDTO getDosenDashboardSummary(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        Dosen dosen = user.getDosen();
        if (dosen == null) {
            throw new IllegalStateException("User ini bukan seorang dosen.");
        }
        
        int totalSksMengajar = dosen.getKelasMengajar().stream()
                .mapToInt(kelas -> kelas.getMataKuliah().getSks())
                .sum();
        
        long krsMenungguPersetujuan = dosen.getMahasiswaBimbingan().stream()
            .filter(mahasiswa -> 
                mahasiswa.getKrsList().stream()
                    .anyMatch(krs -> krs.getStatusPersetujuan() == StatusPersetujuan.DIAJUKAN)
            ).count();
        
        DosenDashboardSummaryDTO dto = new DosenDashboardSummaryDTO();
        dto.setTotalMahasiswaBimbingan(dosen.getMahasiswaBimbingan().size());
        dto.setTotalKelasMengajar(dosen.getKelasMengajar().size());
        dto.setTotalSksMengajar(totalSksMengajar);
        dto.setKrsMenungguPersetujuan(krsMenungguPersetujuan);
        
        return dto;
    }

    private DosenDTO convertToDTO(Dosen dosen) {
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

    // --- HELPER METHOD BARU ---
    private DosenProfileDTO convertToProfileDTO(Dosen dosen) {
        DosenProfileDTO dto = new DosenProfileDTO();
        dto.setNamaLengkap(dosen.getNamaLengkap());
        dto.setNidn(dosen.getNidn());
        if (dosen.getJurusan() != null) {
            dto.setNamaJurusan(dosen.getJurusan().getNamaJurusan());
        }

        BiodataDosen biodata = dosen.getBiodata();
        if (biodata != null) {
            dto.setAlamat(biodata.getAlamat());
            dto.setNomorTelepon(biodata.getNomorTelepon());
            dto.setEmailPribadi(biodata.getEmailPribadi());
            dto.setSpesialisasi(biodata.getSpesialisasi());
        }
        return dto;
    }
}