// java-spring-boot/com/sttis/config/seeder/DosenSeeder.java
package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.BiodataDosen;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.Role;
import com.sttis.models.entities.User;
import com.sttis.models.repos.BiodataDosenRepository;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.RoleRepository;
import com.sttis.models.repos.UserRepository;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Getter
public class DosenSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DosenRepository dosenRepository;
    private final JurusanRepository jurusanRepository;
    private final BiodataDosenRepository biodataDosenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(new Locale("id-ID"));

    private final List<Dosen> createdDosens = new ArrayList<>();

    public DosenSeeder(UserRepository userRepository, RoleRepository roleRepository, DosenRepository dosenRepository, JurusanRepository jurusanRepository, BiodataDosenRepository biodataDosenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dosenRepository = dosenRepository;
        this.jurusanRepository = jurusanRepository;
        this.biodataDosenRepository = biodataDosenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        Role dosenRole = roleRepository.findAll().stream()
                .filter(r -> r.getRoleName().equals("Dosen")).findFirst()
                .orElseThrow(() -> new RuntimeException("Role 'Dosen' tidak ditemukan."));
        List<Jurusan> allJurusan = jurusanRepository.findAll();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/dosen_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {
            
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                String nidn = csvRecord.get("nidn");
                String namaLengkap = csvRecord.get("nama");
                String namaJurusan = csvRecord.get("jurusan");
                String username = csvRecord.get("username");

                // --- PERBAIKAN DI SINI ---
                Jurusan jurusan = allJurusan.stream()
                        .filter(j -> j.getNamaJurusan().equalsIgnoreCase(namaJurusan) 
                                  || j.getFakultas().getNamaFakultas().equalsIgnoreCase(namaJurusan))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Jurusan atau Fakultas '" + namaJurusan + "' untuk dosen '" + namaLengkap + "' tidak ditemukan."));

                // Buat User untuk Dosen
                User dosenUser = new User();
                dosenUser.setUsername(username);
                dosenUser.setPassword(passwordEncoder.encode("dosen")); // Password default
                dosenUser.setRole(dosenRole);
                User savedDosenUser = userRepository.save(dosenUser);

                // Buat Entitas Dosen
                Dosen dosen = new Dosen();
                dosen.setUser(savedDosenUser);
                dosen.setNamaLengkap(namaLengkap);
                dosen.setNidn(nidn);
                dosen.setJurusan(jurusan);
                Dosen savedDosen = dosenRepository.save(dosen);
                createdDosens.add(savedDosen);

                // Buat Biodata Dosen (dengan data acak dari Faker)
                BiodataDosen biodataDosen = new BiodataDosen();
                biodataDosen.setDosen(savedDosen);
                biodataDosen.setAlamat(faker.address().fullAddress());
                biodataDosen.setNomorTelepon(faker.phoneNumber().phoneNumber());
                biodataDosen.setSpesialisasi(faker.job().field());
                biodataDosen.setEmailPribadi(username + "@sttis.ac.id");
                biodataDosenRepository.save(biodataDosen);
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal memproses file dosen_naruto.csv: " + e.getMessage(), e);
        }
        
        System.out.println("Seeder: Data Dosen dari file CSV berhasil dibuat.");
    }
}