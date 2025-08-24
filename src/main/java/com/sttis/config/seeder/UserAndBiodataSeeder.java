package com.sttis.config.seeder;

import com.github.javafaker.Faker;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.repos.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Getter // Lombok untuk membuat getter secara otomatis
public class UserAndBiodataSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;
    private final JurusanRepository jurusanRepository;
    private final BiodataMahasiswaRepository biodataMahasiswaRepository;
    private final BiodataDosenRepository biodataDosenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(new Locale("id-ID"));

    // Lists to store created entities for other seeders
    private User adminUser;
    private final List<Dosen> createdDosens = new ArrayList<>();
    private final List<Mahasiswa> createdMahasiswas = new ArrayList<>();

    public UserAndBiodataSeeder(UserRepository userRepository, RoleRepository roleRepository, MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository, JurusanRepository jurusanRepository, BiodataMahasiswaRepository biodataMahasiswaRepository, BiodataDosenRepository biodataDosenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
        this.jurusanRepository = jurusanRepository;
        this.biodataMahasiswaRepository = biodataMahasiswaRepository;
        this.biodataDosenRepository = biodataDosenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        // Get roles and jurusan
        Role adminRole = roleRepository.findAll().stream().filter(r -> r.getRoleName().equals("Admin")).findFirst().orElseThrow();
        Role dosenRole = roleRepository.findAll().stream().filter(r -> r.getRoleName().equals("Dosen")).findFirst().orElseThrow();
        Role mahasiswaRole = roleRepository.findAll().stream().filter(r -> r.getRoleName().equals("Mahasiswa")).findFirst().orElseThrow();
        Jurusan teologi = jurusanRepository.findAll().stream().filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();
        Jurusan pak = jurusanRepository.findAll().stream().filter(j -> j.getNamaJurusan().equals("S1 Pendidikan Agama Kristen")).findFirst().orElseThrow();

        // 1. Create Admin User
        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRole(adminRole);
        userRepository.save(adminUser);

        // 2. Create Demo Dosen
        User dosenDemoUser = new User();
        dosenDemoUser.setUsername("dosen");
        dosenDemoUser.setPassword(passwordEncoder.encode("dosen"));
        dosenDemoUser.setRole(dosenRole);
        userRepository.save(dosenDemoUser);

        Dosen dosenPA_Demo = new Dosen();
        dosenPA_Demo.setUser(dosenDemoUser);
        dosenPA_Demo.setNamaLengkap("Dr. Glenn Maramis, S.Kom., M.CompSc");
        dosenPA_Demo.setNidn("0912048801");
        dosenPA_Demo.setJurusan(teologi);
        dosenRepository.save(dosenPA_Demo);
        createdDosens.add(dosenPA_Demo);
        
        // 3. Create Other Dosens
        for (int i = 0; i < 5; i++) {
            User dosenUser = new User();
            dosenUser.setUsername(faker.name().username());
            dosenUser.setPassword(passwordEncoder.encode("dosen123"));
            dosenUser.setRole(dosenRole);
            User savedDosenUser = userRepository.save(dosenUser);
            
            Dosen dosen = new Dosen();
            dosen.setUser(savedDosenUser);
            dosen.setNamaLengkap(faker.name().fullName());
            dosen.setNidn(faker.number().digits(10));
            dosen.setJurusan(i % 2 == 0 ? teologi : pak);
            Dosen savedDosen = dosenRepository.save(dosen);
            createdDosens.add(savedDosen);

            BiodataDosen biodataDosen = new BiodataDosen();
            biodataDosen.setDosen(savedDosen);
            biodataDosen.setAlamat(faker.address().fullAddress());
            biodataDosen.setNomorTelepon(faker.phoneNumber().phoneNumber());
            biodataDosen.setSpesialisasi(faker.job().field());
            biodataDosenRepository.save(biodataDosen);
        }

        // 4. Create Demo Mahasiswa
        User mhsDemoUser = new User();
        mhsDemoUser.setUsername("20210118");
        mhsDemoUser.setPassword(passwordEncoder.encode("password"));
        mhsDemoUser.setRole(mahasiswaRole);
        userRepository.save(mhsDemoUser);
        
        Mahasiswa mhsDemo = new Mahasiswa();
        mhsDemo.setUser(mhsDemoUser);
        mhsDemo.setNamaLengkap("Frendy Gerung");
        mhsDemo.setNim("20210118");
        mhsDemo.setJurusan(teologi);
        mhsDemo.setStatus(StatusMahasiswa.AKTIF);
        mhsDemo.setPembimbingAkademik(dosenPA_Demo);
        mahasiswaRepository.save(mhsDemo);
        createdMahasiswas.add(mhsDemo);
        
        BiodataMahasiswa biodataMhsDemo = new BiodataMahasiswa();
        biodataMhsDemo.setMahasiswa(mhsDemo);
        biodataMhsDemo.setAlamat("Jl. Raya Tahuna-Manganitu, Siau Timur");
        biodataMhsDemo.setNomorTelepon("085298937694");
        biodataMhsDemo.setTempatLahir("Manado");
        biodataMhsDemo.setTanggalLahir(LocalDate.of(2003, 8, 17));
        biodataMhsDemo.setJenisKelamin("Laki-laki");
        biodataMahasiswaRepository.save(biodataMhsDemo);

        // 5. Create Other Mahasiswa
        for (int i = 0; i < 10; i++) {
            User mhsUser = new User();
            mhsUser.setUsername(faker.name().username());
            mhsUser.setPassword(passwordEncoder.encode("mahasiswa123"));
            mhsUser.setRole(mahasiswaRole);
            User savedMhsUser = userRepository.save(mhsUser);

            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setUser(savedMhsUser);
            mahasiswa.setNamaLengkap(faker.name().fullName());
            mahasiswa.setNim("2025" + faker.number().digits(4));
            mahasiswa.setJurusan(i % 2 == 0 ? teologi : pak);
            mahasiswa.setStatus(StatusMahasiswa.AKTIF);
            mahasiswa.setPembimbingAkademik(createdDosens.get(i % createdDosens.size()));
            Mahasiswa savedMhs = mahasiswaRepository.save(mahasiswa);
            createdMahasiswas.add(savedMhs);

            BiodataMahasiswa biodataMhs = new BiodataMahasiswa();
            biodataMhs.setMahasiswa(savedMhs);
            biodataMhs.setAlamat(faker.address().fullAddress());
            biodataMhs.setNomorTelepon(faker.phoneNumber().phoneNumber());
            biodataMhs.setTempatLahir(faker.address().city());
            biodataMhs.setTanggalLahir(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            biodataMhs.setJenisKelamin(faker.options().option("Laki-laki", "Perempuan")); 
            biodataMahasiswaRepository.save(biodataMhs);
        }
        
        System.out.println("Seeder: Data User & Biodata berhasil dibuat.");
    }
}