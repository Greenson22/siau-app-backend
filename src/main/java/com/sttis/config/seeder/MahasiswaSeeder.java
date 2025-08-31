package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.BiodataMahasiswa;
import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.Role;
import com.sttis.models.entities.User;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.repos.BiodataMahasiswaRepository;
import com.sttis.models.repos.DosenRepository;
import com.sttis.models.repos.JurusanRepository;
import com.sttis.models.repos.MahasiswaRepository;
import com.sttis.models.repos.RoleRepository;
import com.sttis.models.repos.UserRepository;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Getter
public class MahasiswaSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final JurusanRepository jurusanRepository;
    private final DosenRepository dosenRepository;
    private final BiodataMahasiswaRepository biodataMahasiswaRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(new Locale("id-ID"));

    private final List<Mahasiswa> createdMahasiswas = new ArrayList<>();

    public MahasiswaSeeder(UserRepository userRepository, RoleRepository roleRepository, MahasiswaRepository mahasiswaRepository, JurusanRepository jurusanRepository, DosenRepository dosenRepository, BiodataMahasiswaRepository biodataMahasiswaRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.jurusanRepository = jurusanRepository;
        this.dosenRepository = dosenRepository;
        this.biodataMahasiswaRepository = biodataMahasiswaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        // --- 1. Persiapan Data Awal ---
        Role mahasiswaRole = roleRepository.findAll().stream()
                .filter(r -> r.getRoleName().equals("Mahasiswa")).findFirst().orElseThrow();
        List<Jurusan> allJurusan = jurusanRepository.findAll();
        List<Dosen> allDosens = dosenRepository.findAll();

        if (allDosens.isEmpty()) {
            throw new IllegalStateException("Seeder Dosen harus dijalankan sebelum MahasiswaSeeder.");
        }

        // --- 2. Baca dan Proses File CSV ---
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/mahasiswa_naruto.csv").getInputStream(), StandardCharsets.UTF_8))) {
            
            // Lewati header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String nim = values[0];
                String namaLengkap = values[1];
                String namaJurusan = values[2];

                // Cari jurusan yang sesuai
                Jurusan jurusan = allJurusan.stream()
                        .filter(j -> j.getNamaJurusan().equalsIgnoreCase(namaJurusan))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Jurusan '" + namaJurusan + "' tidak ditemukan di database."));

                // --- 3. Buat Entitas untuk setiap baris ---
                
                // Buat User
                User mhsUser = new User();
                mhsUser.setUsername(nim); // Gunakan NIM sebagai username
                mhsUser.setPassword(passwordEncoder.encode("password")); // Password default
                mhsUser.setRole(mahasiswaRole);
                User savedMhsUser = userRepository.save(mhsUser);

                // Buat Mahasiswa
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setUser(savedMhsUser);
                mahasiswa.setNamaLengkap(namaLengkap);
                mahasiswa.setNim(nim);
                mahasiswa.setJurusan(jurusan);
                mahasiswa.setStatus(StatusMahasiswa.AKTIF);
                // Tetapkan Dosen PA secara acak dari daftar yang ada
                mahasiswa.setPembimbingAkademik(allDosens.get(faker.number().numberBetween(0, allDosens.size())));
                Mahasiswa savedMhs = mahasiswaRepository.save(mahasiswa);
                createdMahasiswas.add(savedMhs);

                // Buat Biodata Mahasiswa (dengan data acak dari Faker)
                BiodataMahasiswa biodataMhs = new BiodataMahasiswa();
                biodataMhs.setMahasiswa(savedMhs);
                biodataMhs.setAlamat(faker.address().fullAddress());
                biodataMhs.setNomorTelepon(faker.phoneNumber().phoneNumber());
                biodataMhs.setTempatLahir(faker.address().city());
                biodataMhs.setTanggalLahir(LocalDate.of(2003, faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)));
                biodataMhs.setJenisKelamin(faker.options().option("Laki-laki", "Perempuan"));
                biodataMahasiswaRepository.save(biodataMhs);
            }

        } catch (Exception e) {
            // Jika terjadi error saat membaca file, lemparkan sebagai runtime exception
            throw new RuntimeException("Gagal membaca atau memproses file CSV: " + e.getMessage(), e);
        }
        
        System.out.println("Seeder: Data Mahasiswa dari mahasiswa_naruto.csv berhasil dibuat.");
    }
}