// java-spring-boot/com/sttis/services/JurusanService.java
package com.sttis.services;

import com.sttis.dto.JurusanDTO;
import com.sttis.models.entities.Fakultas;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.repos.FakultasRepository;
import com.sttis.models.repos.JurusanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JurusanService {

    private final JurusanRepository jurusanRepository;
    private final FakultasRepository fakultasRepository; // <-- DEPENDENCY BARU

    public JurusanService(JurusanRepository jurusanRepository, FakultasRepository fakultasRepository) {
        this.jurusanRepository = jurusanRepository;
        this.fakultasRepository = fakultasRepository; // <-- INJEKSI
    }

    public JurusanDTO createJurusan(JurusanDTO jurusanDTO) {
        // --- LOGIKA DIPERBARUI ---
        Fakultas fakultas = fakultasRepository.findById(jurusanDTO.getFakultasId())
                .orElseThrow(() -> new RuntimeException("Fakultas dengan ID " + jurusanDTO.getFakultasId() + " tidak ditemukan."));

        Jurusan jurusan = new Jurusan();
        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        jurusan.setFakultas(fakultas); // Set objek Fakultas
        
        Jurusan savedJurusan = jurusanRepository.save(jurusan);
        return convertToDTO(savedJurusan);
    }

    @Transactional(readOnly = true)
    public List<JurusanDTO> getAllJurusan() {
        return jurusanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public JurusanDTO updateJurusan(Integer id, JurusanDTO jurusanDTO) {
        // --- LOGIKA DIPERBARUI ---
        Jurusan jurusan = jurusanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jurusan dengan ID " + id + " tidak ditemukan."));
        Fakultas fakultas = fakultasRepository.findById(jurusanDTO.getFakultasId())
                .orElseThrow(() -> new RuntimeException("Fakultas dengan ID " + jurusanDTO.getFakultasId() + " tidak ditemukan."));

        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        jurusan.setFakultas(fakultas);
        
        Jurusan updatedJurusan = jurusanRepository.save(jurusan);
        return convertToDTO(updatedJurusan);
    }

    private JurusanDTO convertToDTO(Jurusan jurusan) {
        // --- LOGIKA DIPERBARUI ---
        JurusanDTO dto = new JurusanDTO();
        dto.setJurusanId(jurusan.getJurusanId());
        dto.setNamaJurusan(jurusan.getNamaJurusan());
        if (jurusan.getFakultas() != null) {
            dto.setFakultasId(jurusan.getFakultas().getFakultasId());
            dto.setNamaFakultas(jurusan.getFakultas().getNamaFakultas());
        }
        return dto;
    }
}