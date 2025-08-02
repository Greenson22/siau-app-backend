package com.sttis.services;

import com.sttis.dto.DosenBiodataUpdateDTO;
import com.sttis.dto.DosenDTO;
import com.sttis.models.entities.BiodataDosen;
import com.sttis.models.entities.Dosen;
import com.sttis.models.repos.BiodataDosenRepository;
import com.sttis.models.repos.DosenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DosenService {

    private final DosenRepository dosenRepository;
    private final BiodataDosenRepository biodataDosenRepository;

    public DosenService(DosenRepository dosenRepository, BiodataDosenRepository biodataDosenRepository) {
        this.dosenRepository = dosenRepository;
        this.biodataDosenRepository = biodataDosenRepository;
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

    @Transactional
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
}