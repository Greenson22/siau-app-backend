package com.sttis.config.seeder;

import com.sttis.models.entities.DetailPaketMatakuliah;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.entities.PaketMatakuliah;
import com.sttis.models.repos.DetailPaketMatakuliahRepository;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
import com.sttis.models.repos.PaketMatakuliahRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaketMatakuliahSeeder {

    private final PaketMatakuliahRepository paketMatakuliahRepository;
    private final DetailPaketMatakuliahRepository detailPaketMatakuliahRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final JurusanRepository jurusanRepository;

    public PaketMatakuliahSeeder(PaketMatakuliahRepository paketMatakuliahRepository, DetailPaketMatakuliahRepository detailPaketMatakuliahRepository, MataKuliahRepository mataKuliahRepository, JurusanRepository jurusanRepository) {
        this.paketMatakuliahRepository = paketMatakuliahRepository;
        this.detailPaketMatakuliahRepository = detailPaketMatakuliahRepository;
        this.mataKuliahRepository = mataKuliahRepository;
        this.jurusanRepository = jurusanRepository;
    }

    public void seed() {
        Jurusan teologi = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();

        // Buat paket untuk setiap semester
        createPaket(teologi, 1, "2021/2022", List.of("TEO101", "TEO102", "PAK101"));
        createPaket(teologi, 2, "2021/2022", List.of("TEO201", "BIB201", "MIS201"));
        createPaket(teologi, 3, "2022/2023", List.of("TEO301", "BIB301", "SEJ301"));
        createPaket(teologi, 4, "2022/2023", List.of("TEO402", "PAK402", "PRA402"));
        createPaket(teologi, 5, "2023/2024", List.of("TEO501", "BIB501", "MIS501"));
        createPaket(teologi, 6, "2023/2024", List.of("TEO601", "BIB601", "PRA601"));
        createPaket(teologi, 7, "2024/2025", List.of("TEO701", "PAK701", "BIB701", "PRA701", "SEJ701"));

        System.out.println("Seeder: Data Paket Mata Kuliah berhasil dibuat.");
    }

    private void createPaket(Jurusan jurusan, int semester, String tahun, List<String> kodeMatkulList) {
        PaketMatakuliah paket = new PaketMatakuliah();
        paket.setNamaPaket("Paket Matakuliah " + jurusan.getNamaJurusan() + " Semester " + semester);
        paket.setJurusan(jurusan);
        paket.setSemesterKe(semester);
        paket.setTahunAkademik(tahun);
        paketMatakuliahRepository.save(paket);

        for (String kodeMk : kodeMatkulList) {
            MataKuliah mk = mataKuliahRepository.findAll().stream()
                    .filter(m -> m.getKodeMatkul().equals(kodeMk)).findFirst().orElseThrow();
            
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(paket);
            detail.setMataKuliah(mk);
            detailPaketMatakuliahRepository.save(detail);
        }
    }
}