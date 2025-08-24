package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.JenisBiaya;
import com.sttis.models.entities.enums.StatusTagihan;
import com.sttis.models.entities.enums.StatusVerifikasi;
import com.sttis.models.repos.KomponenBiayaRepository;
import com.sttis.models.repos.TagihanMahasiswaRepository;
import com.sttis.models.repos.DetailTagihanRepository;
import com.sttis.models.repos.PembayaranRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
public class KeuanganSeeder {

    private final KomponenBiayaRepository komponenBiayaRepository;
    private final TagihanMahasiswaRepository tagihanMahasiswaRepository;
    private final DetailTagihanRepository detailTagihanRepository;
    private final PembayaranRepository pembayaranRepository;
    private final Faker faker = new Faker(new Locale("id-ID"));

    public KeuanganSeeder(KomponenBiayaRepository komponenBiayaRepository, TagihanMahasiswaRepository tagihanMahasiswaRepository, DetailTagihanRepository detailTagihanRepository, PembayaranRepository pembayaranRepository) {
        this.komponenBiayaRepository = komponenBiayaRepository;
        this.tagihanMahasiswaRepository = tagihanMahasiswaRepository;
        this.detailTagihanRepository = detailTagihanRepository;
        this.pembayaranRepository = pembayaranRepository;
    }

    public void seed(List<Mahasiswa> createdMahasiswas) {
        // 1. Create Komponen Biaya
        KomponenBiaya spp = new KomponenBiaya();
        spp.setNamaKomponen("Biaya Kuliah Bulanan");
        spp.setJenisBiaya(JenisBiaya.BULANAN);
        komponenBiayaRepository.save(spp);

        KomponenBiaya pembangunan = new KomponenBiaya();
        pembangunan.setNamaKomponen("Uang Pembangunan");
        pembangunan.setJenisBiaya(JenisBiaya.SEKALI_BAYAR);
        komponenBiayaRepository.save(pembangunan);

        // 2. Create Tagihan and Pembayaran for each Mahasiswa
        Mahasiswa mhsDemo = createdMahasiswas.stream().filter(m -> m.getNim().equals("20210118")).findFirst().orElseThrow();
        for (Mahasiswa mhs : createdMahasiswas) {
            TagihanMahasiswa tagihan = new TagihanMahasiswa();
            tagihan.setMahasiswa(mhs);
            tagihan.setDeskripsiTagihan("Tagihan Semester Ganjil 2024/2025");
            tagihan.setTanggalJatuhTempo(LocalDate.now().plusMonths(1));
            tagihan.setStatus(StatusTagihan.BELUM_LUNAS);
            tagihan.setTotalTagihan(new BigDecimal("3500000"));
            TagihanMahasiswa savedTagihan = tagihanMahasiswaRepository.save(tagihan);

            DetailTagihan detail = new DetailTagihan();
            detail.setTagihanMahasiswa(savedTagihan);
            detail.setKomponenBiaya(spp);
            detail.setJumlah(new BigDecimal("3500000"));
            detailTagihanRepository.save(detail);

            if (mhs.equals(mhsDemo) || faker.bool().bool()) { // Ensure demo mahasiswa has paid
                Pembayaran bayar = new Pembayaran();
                bayar.setTagihan(savedTagihan);
                bayar.setJumlahBayar(new BigDecimal("3500000"));
                bayar.setTanggalBayar(java.time.LocalDateTime.now());
                bayar.setMetodePembayaran("Transfer Bank");
                bayar.setStatusVerifikasi(StatusVerifikasi.BERHASIL);
                pembayaranRepository.save(bayar);
                savedTagihan.setStatus(StatusTagihan.LUNAS);
                tagihanMahasiswaRepository.save(savedTagihan);
            }
        }
        
        System.out.println("Seeder: Data Keuangan berhasil dibuat.");
    }
}