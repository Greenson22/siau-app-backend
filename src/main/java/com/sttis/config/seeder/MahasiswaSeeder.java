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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        Role mahasiswaRole = roleRepository.findAll().stream()
                .filter(r -> r.getRoleName().equals("Mahasiswa")).findFirst().orElseThrow();
        Jurusan teologi = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();
        Jurusan pak = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Pendidikan Agama Kristen")).findFirst().orElseThrow();
        Dosen dosenPA_Demo = dosenRepository.findAll().stream()
                .filter(d -> d.getNidn().equals("0912048801")).findFirst().orElseThrow();
        List<Dosen> allDosens = dosenRepository.findAll();

        // Create Demo Mahasiswa
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

        // Create Other Mahasiswa
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
            mahasiswa.setPembimbingAkademik(allDosens.get(faker.number().numberBetween(0, allDosens.size())));
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
        
        System.out.println("Seeder: Data Mahasiswa berhasil dibuat.");
    }
}