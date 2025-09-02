package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.entities.enums.Semester;
import com.sttis.models.repos.KelasRepository;
import com.sttis.models.repos.MataKuliahRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class KelasSeeder {

    private final KelasRepository kelasRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final Faker faker = new Faker(new Locale("id-ID"));

    public KelasSeeder(KelasRepository kelasRepository, MataKuliahRepository mataKuliahRepository) {
        this.kelasRepository = kelasRepository;
        this.mataKuliahRepository = mataKuliahRepository;
    }

    public void seed(List<Dosen> dosens) {
        // --- PERUBAHAN DI SINI ---
        // 1. Definisikan semua kode mata kuliah yang harus memiliki kelas semester ini
        //    (Tambahkan "NIN102" ke dalam daftar)
        List<String> currentSemesterMkCodes = List.of("NIN101", "NIN102", "TAI101", "GEN101", "MED201");

        // 2. Ambil objek MataKuliah berdasarkan daftar kode di atas
        List<MataKuliah> currentMatkul = mataKuliahRepository.findAll().stream()
                .filter(mk -> currentSemesterMkCodes.contains(mk.getKodeMatkul()))
                .collect(Collectors.toList());

        if (currentMatkul.isEmpty()) {
            System.out.println("Peringatan: Tidak ada mata kuliah yang cocok untuk dibuat kelasnya di KelasSeeder.");
            return;
        }

        // 3. Buat entitas Kelas untuk setiap mata kuliah yang ditemukan
        for(MataKuliah mk : currentMatkul) {
             Kelas kelas = new Kelas();
            kelas.setMataKuliah(mk);
            // Tetapkan Dosen secara acak
            kelas.setDosen(dosens.get(faker.number().numberBetween(0, dosens.size())));
            kelas.setTahunAkademik("2024/2025");
            kelas.setSemester(Semester.GANJIL); // Anggap semester berjalan adalah Ganjil
            kelas.setHari(faker.options().option("Senin", "Selasa", "Rabu", "Kamis", "Jumat"));
            kelas.setJamMulai(LocalTime.of(faker.number().numberBetween(8, 15), 0));
            kelas.setRuangan("R." + faker.number().numberBetween(101, 305));
            kelasRepository.save(kelas);
        }
        
        System.out.println("Seeder: Data Kelas untuk semester berjalan berhasil dibuat.");
    }
}