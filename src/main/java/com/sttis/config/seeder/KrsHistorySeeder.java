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
        Mahasiswa mhsDemo = mahasiswas.stream().filter(m -> m.getNim().equals("20210118")).findFirst().orElseThrow();
        List<MataKuliah> allMatkul = mataKuliahRepository.findAll();

        MataKuliah firstOfSemester7 = allMatkul.stream().filter(mk -> mk.getKodeMatkul().equals("TEO701")).findFirst().orElseThrow();
        List<MataKuliah> historyMatkul = allMatkul.subList(0, allMatkul.indexOf(firstOfSemester7));

        for(MataKuliah mk : historyMatkul) {
            Kelas kelasHistori = new Kelas();
            kelasHistori.setMataKuliah(mk);
            kelasHistori.setDosen(dosens.get(faker.number().numberBetween(0, dosens.size())));
            kelasHistori.setTahunAkademik("HISTORI");
            kelasHistori.setSemester(Semester.GANJIL); // Nilai dummy, tidak relevan
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