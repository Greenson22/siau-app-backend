package com.sttis.config.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final JurusanSeeder jurusanSeeder;
    private final UserSeeder userSeeder; // <-- PERUBAHAN DI SINI
    private final AkademikSeeder akademikSeeder;
    private final KeuanganSeeder keuanganSeeder;
    private final LogAndPengumumanSeeder logAndPengumumanSeeder;

    // Injeksi seeder utama, termasuk UserSeeder yang baru
    public DataSeeder(
        RoleSeeder roleSeeder, 
        JurusanSeeder jurusanSeeder, 
        UserSeeder userSeeder, // <-- PERUBAHAN DI SINI
        AkademikSeeder akademikSeeder, 
        KeuanganSeeder keuanganSeeder, 
        LogAndPengumumanSeeder logAndPengumumanSeeder
    ) {
        this.roleSeeder = roleSeeder;
        this.jurusanSeeder = jurusanSeeder;
        this.userSeeder = userSeeder; // <-- PERUBAHAN DI SINI
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
        
        // Panggil setiap seeder secara berurutan
        roleSeeder.seed();
        // jurusanSeeder.seed();
        userSeeder.seed(); // <-- Cukup panggil ini untuk semua data pengguna

        // Gunakan getter dari userSeeder untuk mendapatkan data yang dibutuhkan seeder lain
        // akademikSeeder.seed(userSeeder.getCreatedDosens(), userSeeder.getCreatedMahasiswas());
        // keuanganSeeder.seed(userSeeder.getCreatedMahasiswas());
        logAndPengumumanSeeder.seed(userSeeder.getAdminUser());

        System.out.println("Seeder selesai dijalankan.");
    }
}