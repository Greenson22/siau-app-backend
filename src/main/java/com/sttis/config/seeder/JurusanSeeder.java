package com.sttis.config.seeder;

import com.sttis.models.entities.Jurusan;
import com.sttis.models.repos.JurusanRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class JurusanSeeder {

    private final JurusanRepository jurusanRepository;

    public JurusanSeeder(JurusanRepository jurusanRepository) {
        this.jurusanRepository = jurusanRepository;
    }

    public void seed() {
        // Membaca dan memproses file CSV
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/jurusan_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {

            // Lewati baris header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String namaJurusan = values[0];
                String fakultas = values[1];

                // Buat dan simpan entitas Jurusan baru
                Jurusan jurusan = new Jurusan();
                jurusan.setNamaJurusan(namaJurusan);
                jurusan.setFakultas(fakultas);
                jurusanRepository.save(jurusan);
            }

        } catch (Exception e) {
            // Lemparkan exception jika terjadi error saat membaca file
            throw new RuntimeException("Gagal membaca atau memproses file jurusan_naruto.csv: " + e.getMessage(), e);
        }

        System.out.println("Seeder: Data Jurusan dari file CSV berhasil dibuat.");
    }
}