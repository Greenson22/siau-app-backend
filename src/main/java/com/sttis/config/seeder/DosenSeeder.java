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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
                .filter(r -> r.getRoleName().equals("Dosen")).findFirst().orElseThrow();
        Jurusan teologi = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Teologi")).findFirst().orElseThrow();
        Jurusan pak = jurusanRepository.findAll().stream()
                .filter(j -> j.getNamaJurusan().equals("S1 Pendidikan Agama Kristen")).findFirst().orElseThrow();

        // Create Demo Dosen
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
        
        // Create Other Dosens
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
        
        System.out.println("Seeder: Data Dosen berhasil dibuat.");
    }
}