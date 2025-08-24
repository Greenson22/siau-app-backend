package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.Semester;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class AkademikSeeder {

    private final MataKuliahRepository mataKuliahRepository;
    private final PaketMatakuliahRepository paketMatakuliahRepository;
    private final DetailPaketMatakuliahRepository detailPaketMatakuliahRepository;
    private final KelasRepository kelasRepository;
    private final KrsRepository krsRepository;
    private final JurusanRepository jurusanRepository;
    private final Faker faker = new Faker(new Locale("id-ID"));

    public AkademikSeeder(MataKuliahRepository mataKuliahRepository, PaketMatakuliahRepository paketMatakuliahRepository, DetailPaketMatakuliahRepository detailPaketMatakuliahRepository, KelasRepository kelasRepository, KrsRepository krsRepository, JurusanRepository jurusanRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
        this.paketMatakuliahRepository = paketMatakuliahRepository;
        this.detailPaketMatakuliahRepository = detailPaketMatakuliahRepository;
        this.kelasRepository = kelasRepository;
        this.krsRepository = krsRepository;
        this.jurusanRepository = jurusanRepository;
    }

    public void seed(List<Dosen> createdDosens, List<Mahasiswa> createdMahasiswas) {
        Jurusan teologi = jurusanRepository.findAll().stream().filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();
        Mahasiswa mhsDemo = createdMahasiswas.stream().filter(m -> m.getNim().equals("20210118")).findFirst().orElseThrow();

        // 1. Create Mata Kuliah dan Paketnya
        List<MataKuliah> semuaMatkulTeologi = createAllMataKuliah(teologi);
        
        // 2. Buat Riwayat KRS untuk Mahasiswa Demo (Semester 1-6)
        createKrsHistory(mhsDemo, createdDosens, semuaMatkulTeologi);
        
        // 3. Buat Kelas untuk Semester 7
        createCurrentClasses(createdDosens, semuaMatkulTeologi);
        
        System.out.println("Seeder: Data Akademik (MK, Paket, Kelas, KRS) berhasil dibuat.");
    }

    private List<MataKuliah> createAllMataKuliah(Jurusan teologi) {
        List<MataKuliah> semuaMatkulTeologi = new ArrayList<>();
        // Semester 1
        MataKuliah teo101 = createMatkul("TEO101", "Pengantar Perjanjian Lama", 3, teologi);
        MataKuliah teo102 = createMatkul("TEO102", "Pengantar Perjanjian Baru", 3, teologi);
        MataKuliah pak101 = createMatkul("PAK101", "Dasar-Dasar PAK", 2, teologi);
        createPaket(teologi, 1, "2021/2022", Arrays.asList(teo101, teo102, pak101));
        semuaMatkulTeologi.addAll(Arrays.asList(teo101, teo102, pak101));

        // ... (Tambahkan semua mata kuliah lainnya seperti di file asli)
        // Semester 2 - 6 ...

        // Semester 7
        MataKuliah teo701 = createMatkul("TEO701", "Teologi Kontemporer", 3, teologi);
        MataKuliah pak701 = createMatkul("PAK701", "Pendidikan Kristen Lanjutan", 3, teologi);
        MataKuliah bib701 = createMatkul("BIB701", "Eksegesis Lanjutan", 3, teologi);
        MataKuliah pra701 = createMatkul("PRA701", "Praktik Pelayanan Lanjutan", 2, teologi);
        MataKuliah sej701 = createMatkul("SEJ701", "Sejarah Gereja Modern", 2, teologi);
        createPaket(teologi, 7, "2024/2025", Arrays.asList(teo701, pak701, bib701, pra701, sej701));
        semuaMatkulTeologi.addAll(Arrays.asList(teo701, pak701, bib701, pra701, sej701));
        
        return semuaMatkulTeologi;
    }

    private void createKrsHistory(Mahasiswa mhsDemo, List<Dosen> dosens, List<MataKuliah> allMatkul) {
        MataKuliah firstOfSemester7 = allMatkul.stream().filter(mk -> mk.getKodeMatkul().equals("TEO701")).findFirst().orElseThrow();
        List<MataKuliah> historyMatkul = allMatkul.subList(0, allMatkul.indexOf(firstOfSemester7));

        for(MataKuliah mk : historyMatkul) {
            Kelas kelasHistori = new Kelas();
            kelasHistori.setMataKuliah(mk);
            kelasHistori.setDosen(dosens.get(faker.number().numberBetween(0, dosens.size())));
            kelasHistori.setTahunAkademik("HISTORI");
            kelasHistori.setSemester(Semester.GANJIL);
            kelasRepository.save(kelasHistori);

            Krs krsHistori = new Krs();
            krsHistori.setMahasiswa(mhsDemo);
            krsHistori.setKelas(kelasHistori);
            krsHistori.setStatusPersetujuan(StatusPersetujuan.DISETUJUI);
            krsHistori.setNilaiHuruf(faker.options().option("A", "A-", "B+", "B"));
            krsHistori.setNilaiAkhir(new BigDecimal(faker.number().randomDouble(2, 80, 95)));
            krsRepository.save(krsHistori);
        }
    }

    private void createCurrentClasses(List<Dosen> dosens, List<MataKuliah> allMatkul) {
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
    }

    private MataKuliah createMatkul(String kode, String nama, int sks, Jurusan jurusan) {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMatkul(kode);
        mk.setNamaMatkul(nama);
        mk.setSks(sks);
        mk.setJurusan(jurusan);
        return mataKuliahRepository.save(mk);
    }

    private void createPaket(Jurusan jurusan, int semester, String tahun, List<MataKuliah> matkulList) {
        PaketMatakuliah paket = new PaketMatakuliah();
        paket.setNamaPaket("Paket Matakuliah " + jurusan.getNamaJurusan() + " Semester " + semester);
        paket.setJurusan(jurusan);
        paket.setSemesterKe(semester);
        paket.setTahunAkademik(tahun);
        paketMatakuliahRepository.save(paket);

        for (MataKuliah mk : matkulList) {
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(paket);
            detail.setMataKuliah(mk);
            detailPaketMatakuliahRepository.save(detail);
        }
    }
}