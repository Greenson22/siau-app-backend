package com.sttis.services;

import com.sttis.dto.DetailPaketDTO;
import com.sttis.dto.PaketKrsInputDTO;
import com.sttis.dto.PaketMatakuliahDTO;
import com.sttis.models.entities.DetailPaketMatakuliah;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.entities.PaketMatakuliah;
import com.sttis.models.repos.DetailPaketMatakuliahRepository;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
import com.sttis.models.repos.PaketMatakuliahRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaketKrsAdminService {

    private final PaketMatakuliahRepository paketRepo;
    private final JurusanRepository jurusanRepo;
    private final MataKuliahRepository matkulRepo;
    private final DetailPaketMatakuliahRepository detailPaketRepo;

    public PaketKrsAdminService(PaketMatakuliahRepository paketRepo, JurusanRepository jurusanRepo, MataKuliahRepository matkulRepo, DetailPaketMatakuliahRepository detailPaketRepo) {
        this.paketRepo = paketRepo;
        this.jurusanRepo = jurusanRepo;
        this.matkulRepo = matkulRepo;
        this.detailPaketRepo = detailPaketRepo;
    }

    public List<PaketMatakuliahDTO> getAllPaket() {
        return paketRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaketMatakuliahDTO createPaket(PaketKrsInputDTO input) {
        Jurusan jurusan = jurusanRepo.findById(input.getJurusanId())
                .orElseThrow(() -> new RuntimeException("Jurusan tidak ditemukan"));

        PaketMatakuliah paket = new PaketMatakuliah();
        paket.setNamaPaket(input.getNamaPaket());
        paket.setJurusan(jurusan);
        paket.setSemester(input.getSemester());
        PaketMatakuliah savedPaket = paketRepo.save(paket);

        List<DetailPaketMatakuliah> details = input.getMataKuliahIds().stream().map(matkulId -> {
            MataKuliah matkul = matkulRepo.findById(matkulId)
                    .orElseThrow(() -> new RuntimeException("Mata Kuliah dengan ID " + matkulId + " tidak ditemukan"));
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(savedPaket);
            detail.setMataKuliah(matkul);
            return detail;
        }).collect(Collectors.toList());

        detailPaketRepo.saveAll(details);
        savedPaket.setDetailPaket(details);

        return convertToDto(savedPaket);
    }

    public PaketMatakuliahDTO updatePaket(Integer paketId, PaketKrsInputDTO input) {
        PaketMatakuliah paket = paketRepo.findById(paketId)
                .orElseThrow(() -> new RuntimeException("Paket KRS tidak ditemukan"));
        Jurusan jurusan = jurusanRepo.findById(input.getJurusanId())
                .orElseThrow(() -> new RuntimeException("Jurusan tidak ditemukan"));

        paket.setNamaPaket(input.getNamaPaket());
        paket.setJurusan(jurusan);
        paket.setSemester(input.getSemester());

        // Hapus detail lama dan buat yang baru (lebih sederhana)
        paket.getDetailPaket().clear();
        List<DetailPaketMatakuliah> newDetails = input.getMataKuliahIds().stream().map(matkulId -> {
            MataKuliah matkul = matkulRepo.findById(matkulId)
                    .orElseThrow(() -> new RuntimeException("Mata Kuliah dengan ID " + matkulId + " tidak ditemukan"));
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(paket);
            detail.setMataKuliah(matkul);
            return detail;
        }).collect(Collectors.toList());
        paket.getDetailPaket().addAll(newDetails);

        PaketMatakuliah updatedPaket = paketRepo.save(paket);
        return convertToDto(updatedPaket);
    }

    private PaketMatakuliahDTO convertToDto(PaketMatakuliah paket) {
        PaketMatakuliahDTO dto = new PaketMatakuliahDTO();
        dto.setPaketId(paket.getPaketId());
        dto.setNamaPaket(paket.getNamaPaket());
        dto.setSemester(paket.getSemester());
        if (paket.getJurusan() != null) {
            dto.setNamaJurusan(paket.getJurusan().getNamaJurusan());
        }

        List<DetailPaketDTO> detailDtos = paket.getDetailPaket().stream().map(detail -> {
            DetailPaketDTO detailDto = new DetailPaketDTO();
            detailDto.setMatkulId(detail.getMataKuliah().getMatkulId()); // Menambahkan matkulId
            detailDto.setKodeMatkul(detail.getMataKuliah().getKodeMatkul());
            detailDto.setNamaMatkul(detail.getMataKuliah().getNamaMatkul());
            return detailDto;
        }).collect(Collectors.toList());

        dto.setDetailPaket(detailDtos);

        int totalSks = paket.getDetailPaket().stream()
                .mapToInt(d -> d.getMataKuliah().getSks())
                .sum();
        dto.setTotalSks(totalSks);

        return dto;
    }
}
