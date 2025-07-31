package com.sttis.services;

import com.sttis.dto.MahasiswaDTO;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.repos.MahasiswaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MahasiswaService {

    private final MahasiswaRepository mahasiswaRepository;

    public MahasiswaService(MahasiswaRepository mahasiswaRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
    }

    public List<MahasiswaDTO> getAllMahasiswa() {
        return mahasiswaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MahasiswaDTO convertToDTO(Mahasiswa mahasiswa) {
        MahasiswaDTO dto = new MahasiswaDTO();
        dto.setMahasiswaId(mahasiswa.getMahasiswaId());
        dto.setNim(mahasiswa.getNim());
        dto.setNamaLengkap(mahasiswa.getNamaLengkap());
        
        if (mahasiswa.getStatus() != null) {
            dto.setStatus(mahasiswa.getStatus().name());
        }
        
        if (mahasiswa.getJurusan() != null) {
            dto.setNamaJurusan(mahasiswa.getJurusan().getNamaJurusan());
        }

        if (mahasiswa.getPembimbingAkademik() != null) {
            dto.setNamaDosenPA(mahasiswa.getPembimbingAkademik().getNamaLengkap());
        }

        return dto;
    }
}