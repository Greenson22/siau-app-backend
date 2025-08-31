package com.sttis.config.seeder;

import com.sttis.models.entities.DetailPaketMatakuliah;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.entities.PaketMatakuliah;
import com.sttis.models.repos.DetailPaketMatakuliahRepository;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
import com.sttis.models.repos.PaketMatakuliahRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        List<Jurusan> allJurusan = jurusanRepository.findAll();
        List<MataKuliah> allMataKuliah = mataKuliahRepository.findAll();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/paket_krs_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {
            
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withHeader().withIgnoreHeaderCase().withTrim());

            for (CSVRecord csvRecord : csvParser) {
                String namaPaket = csvRecord.get("nama_paket");
                String namaJurusan = csvRecord.get("jurusan");
                int semester = Integer.parseInt(csvRecord.get("semester"));
                String[] kodeMkList = csvRecord.get("kode_mk_list").split(",");

                Jurusan jurusan = allJurusan.stream()
                        .filter(j -> j.getNamaJurusan().equalsIgnoreCase(namaJurusan))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Jurusan '" + namaJurusan + "' untuk paket '" + namaPaket + "' tidak ditemukan."));

                // Buat Paket
                PaketMatakuliah paket = new PaketMatakuliah();
                paket.setNamaPaket(namaPaket);
                paket.setJurusan(jurusan);
                paket.setSemester(semester);
                paketMatakuliahRepository.save(paket);

                // Tambahkan Detail Paket (Mata Kuliah)
                for (String kodeMk : kodeMkList) {
                    MataKuliah mk = allMataKuliah.stream()
                            .filter(m -> m.getKodeMatkul().equalsIgnoreCase(kodeMk.trim()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Mata Kuliah dengan kode '" + kodeMk + "' tidak ditemukan."));
                    
                    DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
                    detail.setPaketMatakuliah(paket);
                    detail.setMataKuliah(mk);
                    detailPaketMatakuliahRepository.save(detail);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal memproses file paket_krs_naruto.csv: " + e.getMessage(), e);
        }

        System.out.println("Seeder: Data Paket Mata Kuliah dari file CSV berhasil dibuat.");
    }
}