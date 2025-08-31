package com.sttis.config.seeder;

import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.MataKuliah;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MataKuliahRepository;
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
public class MataKuliahSeeder {

    private final MataKuliahRepository mataKuliahRepository;
    private final JurusanRepository jurusanRepository;

    public MataKuliahSeeder(MataKuliahRepository mataKuliahRepository, JurusanRepository jurusanRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
        this.jurusanRepository = jurusanRepository;
    }

    public void seed() {
        List<Jurusan> allJurusan = jurusanRepository.findAll();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/matakuliah_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                String kodeMk = csvRecord.get("kode_mk");
                String namaMk = csvRecord.get("nama_mk");
                int sks = Integer.parseInt(csvRecord.get("sks"));
                String namaJurusan = csvRecord.get("jurusan");

                Jurusan jurusan = allJurusan.stream()
                        .filter(j -> j.getNamaJurusan().equalsIgnoreCase(namaJurusan))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Jurusan '" + namaJurusan + "' untuk mata kuliah '" + namaMk + "' tidak ditemukan."));

                MataKuliah mk = new MataKuliah();
                mk.setKodeMatkul(kodeMk);
                mk.setNamaMatkul(namaMk);
                mk.setSks(sks);
                mk.setJurusan(jurusan);
                mataKuliahRepository.save(mk);
            }

        } catch (Exception e) {
            throw new RuntimeException("Gagal memproses file matakuliah_naruto.csv: " + e.getMessage(), e);
        }

        System.out.println("Seeder: Data Mata Kuliah dari file CSV berhasil dibuat.");
    }
}