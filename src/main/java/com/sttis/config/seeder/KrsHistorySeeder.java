package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.Semester;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.KelasRepository;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.MataKuliahRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class KrsHistorySeeder {

    private final KelasRepository kelasRepository;
    private final KrsRepository krsRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final Faker faker = new Faker(new Locale("id-ID"));

    public KrsHistorySeeder(KelasRepository kelasRepository, KrsRepository krsRepository, MataKuliahRepository mataKuliahRepository) {
        this.kelasRepository = kelasRepository;
        this.krsRepository = krsRepository;
        this.mataKuliahRepository = mataKuliahRepository;
    }

    public void seed(List<Dosen> dosens, List<Mahasiswa> mahasiswas) {
        // 1. Cari mahasiswa demo "Uzumaki Naruto" dengan NIM 20240101
        Mahasiswa mhsDemo = mahasiswas.stream()
                .filter(m -> m.getNim().equals("20240101"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Mahasiswa demo dengan NIM 20240101 tidak ditemukan."));

        // 2. Ambil beberapa mata kuliah yang sudah ada untuk dijadikan riwayat
        List<String> historyMkCodes = List.of("NIN101", "NIN102", "TAI101");
        List<MataKuliah> historyMatkul = mataKuliahRepository.findAll().stream()
                .filter(mk -> historyMkCodes.contains(mk.getKodeMatkul()))
                .collect(Collectors.toList());
        
        if (historyMatkul.size() != historyMkCodes.size()) {
             System.out.println("Peringatan: Tidak semua mata kuliah untuk KrsHistorySeeder ditemukan.");
        }

        // 3. Buat kelas dan KRS histori untuk setiap mata kuliah tersebut
        for (MataKuliah mk : historyMatkul) {
            Kelas kelasHistori = new Kelas();
            kelasHistori.setMataKuliah(mk);
            kelasHistori.setDosen(dosens.get(faker.number().numberBetween(0, dosens.size())));
            kelasHistori.setTahunAkademik("2023/2024"); // Tahun akademik lampau
            kelasHistori.setSemester(Semester.GANJIL); // Semester lampau
            kelasRepository.save(kelasHistori);

            Krs krsHistori = new Krs();
            krsHistori.setMahasiswa(mhsDemo);
            krsHistori.setKelas(kelasHistori);
            krsHistori.setStatusPersetujuan(StatusPersetujuan.DISETUJUI);
            krsHistori.setNilaiHuruf(faker.options().option("A", "A-", "B+", "B"));
            krsHistori.setNilaiAkhir(new BigDecimal(faker.number().randomDouble(2, 80, 95)));
            krsRepository.save(krsHistori);
        }
        
        System.out.println("Seeder: Data Riwayat KRS berhasil dibuat.");
    }
}