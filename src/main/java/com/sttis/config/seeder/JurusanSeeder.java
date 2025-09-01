// java-spring-boot/com/sttis/config/seeder/JurusanSeeder.java
package com.sttis.config.seeder;

import com.sttis.models.entities.Fakultas;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.repos.FakultasRepository;
import com.sttis.models.repos.JurusanRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class JurusanSeeder {

    private final JurusanRepository jurusanRepository;
    private final FakultasRepository fakultasRepository; // <-- DEPENDENCY BARU

    public JurusanSeeder(JurusanRepository jurusanRepository, FakultasRepository fakultasRepository) {
        this.jurusanRepository = jurusanRepository;
        this.fakultasRepository = fakultasRepository; // <-- INJEKSI
    }

    public void seed() {
        // --- LOGIKA SEEDER DIPERBARUI TOTAL ---
        Map<String, Fakultas> fakultasMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/jurusan_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {

            br.readLine(); // Lewati header

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String namaJurusan = values[0];
                String namaFakultas = values[1];

                // Cek apakah fakultas sudah ada di map, jika tidak, buat baru
                Fakultas fakultas = fakultasMap.computeIfAbsent(namaFakultas, nf -> {
                    Fakultas newFakultas = new Fakultas();
                    newFakultas.setNamaFakultas(nf);
                    return fakultasRepository.save(newFakultas);
                });

                // Buat dan simpan entitas Jurusan
                Jurusan jurusan = new Jurusan();
                jurusan.setNamaJurusan(namaJurusan);
                jurusan.setFakultas(fakultas);
                jurusanRepository.save(jurusan);
            }

        } catch (Exception e) {
            throw new RuntimeException("Gagal membaca atau memproses file jurusan_naruto.csv: " + e.getMessage(), e);
        }

        System.out.println("Seeder: Data Fakultas dan Jurusan dari file CSV berhasil dibuat.");
    }
}