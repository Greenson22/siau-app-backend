package com.sttis.services;

import com.sttis.dto.JurusanDTO;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.repos.JurusanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JurusanService {

    private final JurusanRepository jurusanRepository;

    public JurusanService(JurusanRepository jurusanRepository) {
        this.jurusanRepository = jurusanRepository;
    }

    /**
     * Membuat jurusan baru berdasarkan data dari DTO.
     * @param jurusanDTO Data untuk jurusan baru.
     * @return JurusanDTO dari entitas yang baru disimpan.
     */
    public JurusanDTO createJurusan(JurusanDTO jurusanDTO) {
        if (jurusanDTO.getNamaJurusan() == null || jurusanDTO.getNamaJurusan().isBlank()) {
            throw new IllegalArgumentException("Nama jurusan tidak boleh kosong.");
        }
        if (jurusanDTO.getFakultas() == null || jurusanDTO.getFakultas().isBlank()) {
            throw new IllegalArgumentException("Nama fakultas tidak boleh kosong.");
        }

        Jurusan jurusan = new Jurusan();
        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        jurusan.setFakultas(jurusanDTO.getFakultas());
        
        Jurusan savedJurusan = jurusanRepository.save(jurusan);
        return convertToDTO(savedJurusan);
    }

    /**
     * Mengambil semua data jurusan dari database.
     * @return Daftar semua JurusanDTO.
     */
    public List<JurusanDTO> getAllJurusan() {
        return jurusanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Helper method untuk mengubah entitas Jurusan menjadi JurusanDTO.
     * @param jurusan Entitas Jurusan.
     * @return Objek JurusanDTO.
     */
    private JurusanDTO convertToDTO(Jurusan jurusan) {
        JurusanDTO dto = new JurusanDTO();
        dto.setJurusanId(jurusan.getJurusanId());
        dto.setNamaJurusan(jurusan.getNamaJurusan());
        dto.setFakultas(jurusan.getFakultas());
        return dto;
    }
}