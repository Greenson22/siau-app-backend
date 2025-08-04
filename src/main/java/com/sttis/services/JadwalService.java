package com.sttis.services;

import com.sttis.dto.JadwalUjianDTO;
import com.sttis.dto.KelasDTO;
import com.sttis.models.entities.JadwalUjian;
import com.sttis.models.entities.Kelas;
import com.sttis.models.repos.JadwalUjianRepository;
import com.sttis.models.repos.KelasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JadwalService {

    private final JadwalUjianRepository jadwalUjianRepository;
    private final KelasRepository kelasRepository;

    public JadwalService(JadwalUjianRepository jadwalUjianRepository, KelasRepository kelasRepository) {
        this.jadwalUjianRepository = jadwalUjianRepository;
        this.kelasRepository = kelasRepository;
    }

    /**
     * Mengambil semua jadwal ujian yang akan datang.
     */
    public List<JadwalUjianDTO> getJadwalUjianAktif() {
        // Mengambil semua ujian yang tanggalnya hari ini atau setelahnya
        return jadwalUjianRepository.findAll().stream()
                .filter(ujian -> !ujian.getTanggalUjian().isBefore(LocalDate.now()))
                .map(this::convertToJadwalUjianDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil daftar semua kelas yang ditawarkan.
     */
    public List<KelasDTO> getKelasDitawarkan() {
        return kelasRepository.findAll().stream()
                .map(this::convertToKelasDTO)
                .collect(Collectors.toList());
    }

    // Helper untuk konversi ke DTO
    private JadwalUjianDTO convertToJadwalUjianDTO(JadwalUjian ujian) {
        JadwalUjianDTO dto = new JadwalUjianDTO();
        dto.setMataKuliah(ujian.getKelas().getMataKuliah().getNamaMatkul());
        dto.setJenisUjian(ujian.getJenisUjian().name());
        dto.setTanggalUjian(ujian.getTanggalUjian());
        dto.setJamMulai(ujian.getJamMulai());
        dto.setJamSelesai(ujian.getJamSelesai());
        dto.setRuangan(ujian.getRuangan());
        dto.setDosenPengampu(ujian.getKelas().getDosen().getNamaLengkap());
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
}