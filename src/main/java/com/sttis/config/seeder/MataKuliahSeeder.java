package com.sttis.config.seeder;

import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
import org.springframework.stereotype.Component;

@Component
public class MataKuliahSeeder {

    private final MataKuliahRepository mataKuliahRepository;
    private final JurusanRepository jurusanRepository;

    public MataKuliahSeeder(MataKuliahRepository mataKuliahRepository, JurusanRepository jurusanRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
        this.jurusanRepository = jurusanRepository;
    }

    public void seed() {
        Jurusan teologi = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();

        // Semester 1
        createMatkul("TEO101", "Pengantar Perjanjian Lama", 3, teologi);
        createMatkul("TEO102", "Pengantar Perjanjian Baru", 3, teologi);
        createMatkul("PAK101", "Dasar-Dasar PAK", 2, teologi);

        // Semester 2
        createMatkul("TEO201", "Teologi Sistematika I", 3, teologi);
        createMatkul("BIB201", "Bahasa Yunani I", 2, teologi);
        createMatkul("MIS201", "Misiologi I", 2, teologi);

        // Semester 3
        createMatkul("TEO301", "Teologi Sistematika II", 3, teologi);
        createMatkul("BIB301", "Bahasa Ibrani I", 2, teologi);
        createMatkul("SEJ301", "Sejarah Gereja Awal", 3, teologi);

        // Semester 4
        createMatkul("TEO402", "Hermeneutik", 3, teologi);
        createMatkul("PAK402", "Psikologi Perkembangan", 3, teologi);
        createMatkul("PRA402", "Homiletika I", 2, teologi);
        
        // Semester 5
        createMatkul("TEO501", "Etika Kristen", 3, teologi);
        createMatkul("BIB501", "Eksegesis Perjanjian Lama", 3, teologi);
        createMatkul("MIS501", "Apologetika", 2, teologi);

        // Semester 6
        createMatkul("TEO601", "Dogmatika", 3, teologi);
        createMatkul("BIB601", "Eksegesis Perjanjian Baru", 3, teologi);
        createMatkul("PRA601", "Pastoral Konseling", 2, teologi);

        // Semester 7
        createMatkul("TEO701", "Teologi Kontemporer", 3, teologi);
        createMatkul("PAK701", "Pendidikan Kristen Lanjutan", 3, teologi);
        createMatkul("BIB701", "Eksegesis Lanjutan", 3, teologi);
        createMatkul("PRA701", "Praktik Pelayanan Lanjutan", 2, teologi);
        createMatkul("SEJ701", "Sejarah Gereja Modern", 2, teologi);
        
        System.out.println("Seeder: Data Mata Kuliah berhasil dibuat.");
    }

    private void createMatkul(String kode, String nama, int sks, Jurusan jurusan) {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMatkul(kode);
        mk.setNamaMatkul(nama);
        mk.setSks(sks);
        mk.setJurusan(jurusan);
        mataKuliahRepository.save(mk);
    }
}