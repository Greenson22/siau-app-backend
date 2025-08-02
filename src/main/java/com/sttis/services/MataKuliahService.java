package com.sttis.services;

import com.sttis.dto.MataKuliahDTO;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MataKuliahService {

    private final MataKuliahRepository mataKuliahRepository;
    private final JurusanRepository jurusanRepository;

    public MataKuliahService(MataKuliahRepository mataKuliahRepository, JurusanRepository jurusanRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
        this.jurusanRepository = jurusanRepository;
    }

    /**
     * Menambah mata kuliah baru ke database.
     * @param dto Data mata kuliah dari client.
     * @return DTO dari mata kuliah yang berhasil disimpan.
     */
    public MataKuliahDTO createMataKuliah(MataKuliahDTO dto) {
        // Cari jurusan berdasarkan ID
        Jurusan jurusan = jurusanRepository.findById(dto.getJurusanId())
                .orElseThrow(() -> new RuntimeException("Jurusan dengan ID " + dto.getJurusanId() + " tidak ditemukan."));

        // Buat entitas baru
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMatkul(dto.getKodeMatkul());
        mataKuliah.setNamaMatkul(dto.getNamaMatkul());
        mataKuliah.setSks(dto.getSks());
        mataKuliah.setJurusan(jurusan);

        // Simpan ke database
        MataKuliah savedMataKuliah = mataKuliahRepository.save(mataKuliah);
        
        return convertToDTO(savedMataKuliah);
    }

    /**
     * Mengambil semua mata kuliah dari database.
     * @return Daftar semua MataKuliahDTO.
     */
    public List<MataKuliahDTO> getAllMataKuliah() {
        return mataKuliahRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method untuk mengubah entitas MataKuliah menjadi DTO.
     * @param mataKuliah Entitas MataKuliah.
     * @return Objek MataKuliahDTO.
     */
    private MataKuliahDTO convertToDTO(MataKuliah mataKuliah) {
        MataKuliahDTO dto = new MataKuliahDTO();
        dto.setMatkulId(mataKuliah.getMatkulId());
        dto.setKodeMatkul(mataKuliah.getKodeMatkul());
        dto.setNamaMatkul(mataKuliah.getNamaMatkul());
        dto.setSks(mataKuliah.getSks());
        if (mataKuliah.getJurusan() != null) {
            dto.setJurusanId(mataKuliah.getJurusan().getJurusanId());
            dto.setNamaJurusan(mataKuliah.getJurusan().getNamaJurusan());
        }
        return dto;
    }
}