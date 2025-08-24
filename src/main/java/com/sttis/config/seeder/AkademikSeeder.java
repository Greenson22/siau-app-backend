package com.sttis.config.seeder;

import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Mahasiswa;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class AkademikSeeder {

    private final MataKuliahSeeder mataKuliahSeeder;
    private final PaketMatakuliahSeeder paketMatakuliahSeeder;
    private final KrsHistorySeeder krsHistorySeeder;
    private final KelasSeeder kelasSeeder;

    public AkademikSeeder(MataKuliahSeeder mataKuliahSeeder, PaketMatakuliahSeeder paketMatakuliahSeeder, KrsHistorySeeder krsHistorySeeder, KelasSeeder kelasSeeder) {
        this.mataKuliahSeeder = mataKuliahSeeder;
        this.paketMatakuliahSeeder = paketMatakuliahSeeder;
        this.krsHistorySeeder = krsHistorySeeder;
        this.kelasSeeder = kelasSeeder;
    }

    @Transactional
    public void seed(List<Dosen> createdDosens, List<Mahasiswa> createdMahasiswas) {
        // Panggil setiap seeder akademik secara berurutan
        mataKuliahSeeder.seed();
        paketMatakuliahSeeder.seed();
        krsHistorySeeder.seed(createdDosens, createdMahasiswas);
        kelasSeeder.seed(createdDosens);
        
        System.out.println("Seeder: Semua data Akademik berhasil dibuat.");
    }
}