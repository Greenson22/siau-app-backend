// main/java/com/sttis/services/KeuanganService.java
package com.sttis.services;

import com.sttis.dto.*;
import com.sttis.dto.TagihanInputDTO.RincianInputDTO;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.StatusTagihan;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KeuanganService {

    private final TagihanMahasiswaRepository tagihanRepo;
    private final DetailTagihanRepository detailTagihanRepo;
    private final PembayaranRepository pembayaranRepo;
    private final MahasiswaRepository mahasiswaRepo;
    private final KomponenBiayaRepository komponenBiayaRepo;
    private final UserRepository userRepo;

    public KeuanganService(TagihanMahasiswaRepository tagihanRepo, DetailTagihanRepository detailTagihanRepo, PembayaranRepository pembayaranRepo, MahasiswaRepository mahasiswaRepo, KomponenBiayaRepository komponenBiayaRepo, UserRepository userRepo) {
        this.tagihanRepo = tagihanRepo;
        this.detailTagihanRepo = detailTagihanRepo;
        this.pembayaranRepo = pembayaranRepo;
        this.mahasiswaRepo = mahasiswaRepo;
        this.komponenBiayaRepo = komponenBiayaRepo;
        this.userRepo = userRepo;
    }

    // --- Logika untuk Mahasiswa ---
    public List<TagihanDTO> getMyTagihan(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);
        return tagihanRepo.findByMahasiswa(mahasiswa).stream()
                .map(this::convertToTagihanDTO)
                .collect(Collectors.toList());
    }

    /**
     * METHOD BARU: Mengambil riwayat pembayaran untuk mahasiswa yang sedang login.
     */
    public List<PembayaranDTO> getMyPembayaran(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);
        
        // Ambil semua tagihan mahasiswa, lalu kumpulkan semua pembayarannya menjadi satu list.
        return tagihanRepo.findByMahasiswa(mahasiswa).stream()
                .flatMap(tagihan -> tagihan.getPembayaranList().stream())
                .map(this::convertToPembayaranDTO)
                .collect(Collectors.toList());
    }


    public void konfirmasiPembayaran(PembayaranInputDTO input, String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);
        TagihanMahasiswa tagihan = tagihanRepo.findById(input.getTagihanId())
                .orElseThrow(() -> new RuntimeException("Tagihan tidak ditemukan."));

        if (!tagihan.getMahasiswa().equals(mahasiswa)) {
            throw new SecurityException("Anda tidak berhak mengakses tagihan ini.");
        }

        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setTagihan(tagihan);
        pembayaran.setJumlahBayar(input.getJumlahBayar());
        pembayaran.setMetodePembayaran(input.getMetodePembayaran());
        pembayaran.setBuktiBayar(input.getBuktiBayar());
        pembayaran.setTanggalBayar(LocalDateTime.now());
        pembayaran.setStatusVerifikasi(StatusVerifikasi.PENDING);
        pembayaranRepo.save(pembayaran);
    }

    // --- Logika untuk Admin ---
    public List<TagihanDTO> getAllTagihan() {
        return tagihanRepo.findAll().stream()
                .map(this::convertToTagihanDTO)
                .collect(Collectors.toList());
    }

    public TagihanDTO getTagihanById(Integer id) {
        return tagihanRepo.findById(id)
                .map(this::convertToTagihanDTO)
                .orElseThrow(() -> new RuntimeException("Tagihan tidak ditemukan."));
    }

    public TagihanDTO createTagihan(TagihanInputDTO input) {
        Mahasiswa mahasiswa = mahasiswaRepo.findById(input.getMahasiswaId())
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan."));

        TagihanMahasiswa tagihan = new TagihanMahasiswa();
        tagihan.setMahasiswa(mahasiswa);
        tagihan.setDeskripsiTagihan(input.getDeskripsiTagihan());
        tagihan.setTanggalJatuhTempo(input.getTanggalJatuhTempo());
        tagihan.setStatus(StatusTagihan.BELUM_LUNAS);

        BigDecimal total = input.getRincian().stream()
                .map(RincianInputDTO::getJumlah)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        tagihan.setTotalTagihan(total);
        
        TagihanMahasiswa savedTagihan = tagihanRepo.save(tagihan);

        List<DetailTagihan> detailList = new ArrayList<>();
        for (TagihanInputDTO.RincianInputDTO rincian : input.getRincian()) {
            KomponenBiaya komponen = komponenBiayaRepo.findById(rincian.getKomponenId())
                    .orElseThrow(() -> new RuntimeException("Komponen biaya tidak ditemukan."));
            DetailTagihan detail = new DetailTagihan();
            detail.setTagihanMahasiswa(savedTagihan);
            detail.setKomponenBiaya(komponen);
            detail.setJumlah(rincian.getJumlah());
            detail.setKeterangan(rincian.getKeterangan());
            detailList.add(detailTagihanRepo.save(detail));
        }
        savedTagihan.setDetailTagihanList(detailList);
        return convertToTagihanDTO(savedTagihan);
    }

    public List<PembayaranDTO> getAllPembayaran() {
        return pembayaranRepo.findAll().stream()
                .map(this::convertToPembayaranDTO)
                .collect(Collectors.toList());
    }

    public void verifikasiPembayaran(Integer pembayaranId, VerifikasiPembayaranDTO input) {
        Pembayaran pembayaran = pembayaranRepo.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Data pembayaran tidak ditemukan."));
        
        StatusVerifikasi status = StatusVerifikasi.valueOf(input.getStatusVerifikasi().toUpperCase());
        pembayaran.setStatusVerifikasi(status);
        
        if (status == StatusVerifikasi.BERHASIL) {
            TagihanMahasiswa tagihan = pembayaran.getTagihan();
            // Logika sederhana: anggap lunas jika sudah diverifikasi
            tagihan.setStatus(StatusTagihan.LUNAS);
            tagihanRepo.save(tagihan);
        }
        pembayaranRepo.save(pembayaran);
    }

    // --- Helper Methods ---
    private Mahasiswa getMahasiswaFromUsername(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        if (user.getMahasiswa() == null) throw new IllegalStateException("User ini bukan mahasiswa.");
        return user.getMahasiswa();
    }
    
    private TagihanDTO convertToTagihanDTO(TagihanMahasiswa tagihan) {
        TagihanDTO dto = new TagihanDTO();
        dto.setTagihanId(tagihan.getTagihanId());
        dto.setMahasiswaId(tagihan.getMahasiswa().getMahasiswaId());
        dto.setNamaMahasiswa(tagihan.getMahasiswa().getNamaLengkap());
        dto.setDeskripsiTagihan(tagihan.getDeskripsiTagihan());
        dto.setTotalTagihan(tagihan.getTotalTagihan());
        dto.setTanggalJatuhTempo(tagihan.getTanggalJatuhTempo());
        dto.setStatus(tagihan.getStatus().name());
        dto.setRincian(tagihan.getDetailTagihanList().stream().map(d -> {
            RincianTagihanDTO r = new RincianTagihanDTO();
            r.setNamaKomponen(d.getKomponenBiaya().getNamaKomponen());
            r.setJumlah(d.getJumlah());
            r.setKeterangan(d.getKeterangan());
            return r;
        }).collect(Collectors.toList()));
        return dto;
    }

    private PembayaranDTO convertToPembayaranDTO(Pembayaran pembayaran) {
        PembayaranDTO dto = new PembayaranDTO();
        dto.setPembayaranId(pembayaran.getPembayaranId());
        dto.setTagihanId(pembayaran.getTagihan().getTagihanId());
        dto.setNamaMahasiswa(pembayaran.getTagihan().getMahasiswa().getNamaLengkap());
        dto.setDeskripsiTagihan(pembayaran.getTagihan().getDeskripsiTagihan()); // <-- BARIS BARU
        dto.setTanggalBayar(pembayaran.getTanggalBayar());
        dto.setJumlahBayar(pembayaran.getJumlahBayar());
        dto.setMetodePembayaran(pembayaran.getMetodePembayaran());
        dto.setStatusVerifikasi(pembayaran.getStatusVerifikasi().name());
        return dto;
    }
}