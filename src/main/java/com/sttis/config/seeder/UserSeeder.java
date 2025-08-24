package com.sttis.config.seeder;

import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class UserSeeder {

    private final AdminSeeder adminSeeder;
    private final DosenSeeder dosenSeeder;
    private final MahasiswaSeeder mahasiswaSeeder;

    // Injeksi semua seeder pengguna
    public UserSeeder(AdminSeeder adminSeeder, DosenSeeder dosenSeeder, MahasiswaSeeder mahasiswaSeeder) {
        this.adminSeeder = adminSeeder;
        this.dosenSeeder = dosenSeeder;
        this.mahasiswaSeeder = mahasiswaSeeder;
    }

    public void seed() {
        adminSeeder.seed();
        dosenSeeder.seed();
        mahasiswaSeeder.seed();

        System.out.println("Seeder: Semua data User & Biodata berhasil dibuat.");
    }
    
    // Getter untuk data yang dibutuhkan seeder lain
    public User getAdminUser() {
        return adminSeeder.getAdminUser();
    }
    
    public List<Dosen> getCreatedDosens() {
        return dosenSeeder.getCreatedDosens();
    }
    
    public List<Mahasiswa> getCreatedMahasiswas() {
        return mahasiswaSeeder.getCreatedMahasiswas();
    }
}