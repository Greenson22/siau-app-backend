// java-spring-boot/com/sttis/services/FakultasService.java
package com.sttis.services;

import com.sttis.dto.FakultasDTO;
import com.sttis.models.entities.Fakultas;
import com.sttis.models.repos.FakultasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FakultasService {

    private final FakultasRepository fakultasRepository;

    public FakultasService(FakultasRepository fakultasRepository) {
        this.fakultasRepository = fakultasRepository;
    }

    public FakultasDTO createFakultas(FakultasDTO fakultasDTO) {
        Fakultas fakultas = new Fakultas();
        fakultas.setNamaFakultas(fakultasDTO.getNamaFakultas());
        Fakultas savedFakultas = fakultasRepository.save(fakultas);
        return convertToDTO(savedFakultas);
    }

    @Transactional(readOnly = true)
    public List<FakultasDTO> getAllFakultas() {
        return fakultasRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FakultasDTO convertToDTO(Fakultas fakultas) {
        FakultasDTO dto = new FakultasDTO();
        dto.setFakultasId(fakultas.getFakultasId());
        dto.setNamaFakultas(fakultas.getNamaFakultas());
        return dto;
    }
}