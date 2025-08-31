package com.sttis.config.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final JurusanSeeder jurusanSeeder;
    private final UserSeeder userSeeder;
    private final DosenSeeder dosenSeeder; // Tambahkan DosenSeeder
    private final MahasiswaSeeder mahasiswaSeeder; // Tambahkan MahasiswaSeeder
    private final AkademikSeeder akademikSeeder;
    private final KeuanganSeeder keuanganSeeder;
    private final LogAndPengumumanSeeder logAndPengumumanSeeder;

    public DataSeeder(
        RoleSeeder roleSeeder, 
        JurusanSeeder jurusanSeeder, 
        UserSeeder userSeeder,
        DosenSeeder dosenSeeder, // Injeksi DosenSeeder
        MahasiswaSeeder mahasiswaSeeder, // Injeksi MahasiswaSeeder
        AkademikSeeder akademikSeeder, 
        KeuanganSeeder keuanganSeeder, 
        LogAndPengumumanSeeder logAndPengumumanSeeder
    ) {
        this.roleSeeder = roleSeeder;
        this.jurusanSeeder = jurusanSeeder;
        this.userSeeder = userSeeder;
        this.dosenSeeder = dosenSeeder; // Set DosenSeeder
        this.mahasiswaSeeder = mahasiswaSeeder; // Set MahasiswaSeeder
        this.akademikSeeder = akademikSeeder;
        this.keuanganSeeder = keuanganSeeder;
        this.logAndPengumumanSeeder = logAndPengumumanSeeder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleSeeder.isSeeded()) {
            System.out.println("Data sudah ada, seeder tidak dijalankan.");
            return;
        }

        System.out.println("Menjalankan Seeder untuk semua tabel...");
        
        // --- URUTAN SEEDER YANG BENAR ---
        roleSeeder.seed();
        jurusanSeeder.seed();
        userSeeder.seed(); // Hanya membuat data admin
        dosenSeeder.seed(); // Buat data Dosen DULU
        mahasiswaSeeder.seed(); // BARU buat data Mahasiswa
        
        // Gunakan getter dari seeder yang sudah dijalankan
        akademikSeeder.seed(dosenSeeder.getCreatedDosens(), mahasiswaSeeder.getCreatedMahasiswas());
        keuanganSeeder.seed(mahasiswaSeeder.getCreatedMahasiswas());
        logAndPengumumanSeeder.seed(userSeeder.getAdminUser());

        System.out.println("Seeder selesai dijalankan.");
    }
}