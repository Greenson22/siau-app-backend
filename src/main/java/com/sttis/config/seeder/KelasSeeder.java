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
        List<MataKuliah> allMatkul = mataKuliahRepository.findAll();
        MataKuliah firstOfSemester7 = allMatkul.stream().filter(mk -> mk.getKodeMatkul().equals("TEO701")).findFirst().orElseThrow();
        List<MataKuliah> currentMatkul = allMatkul.subList(allMatkul.indexOf(firstOfSemester7), allMatkul.size());

        for(MataKuliah mk : currentMatkul) {
             Kelas kelas = new Kelas();
            kelas.setMataKuliah(mk);
            kelas.setDosen(dosens.get(faker.number().numberBetween(0, dosens.size())));
            kelas.setTahunAkademik("2024/2025");
            kelas.setSemester(Semester.GANJIL);
            kelas.setHari(faker.options().option("Senin", "Selasa", "Rabu", "Kamis", "Jumat"));
            kelas.setJamMulai(LocalTime.of(faker.number().numberBetween(8, 15), 0));
            kelas.setRuangan("R." + faker.number().numberBetween(101, 305));
            kelasRepository.save(kelas);
        }
        System.out.println("Seeder: Data Kelas untuk semester berjalan berhasil dibuat.");
    }
}