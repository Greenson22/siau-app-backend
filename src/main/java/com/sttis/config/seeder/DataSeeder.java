package com.sttis.config.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final JurusanSeeder jurusanSeeder;
    private final UserAndBiodataSeeder userAndBiodataSeeder;
    private final AkademikSeeder akademikSeeder;
    private final KeuanganSeeder keuanganSeeder;
    private final LogAndPengumumanSeeder logAndPengumumanSeeder;

    // Injeksi semua seeder sebagai dependensi
    public DataSeeder(
        RoleSeeder roleSeeder, 
        JurusanSeeder jurusanSeeder, 
        UserAndBiodataSeeder userAndBiodataSeeder, 
        AkademikSeeder akademikSeeder, 
        KeuanganSeeder keuanganSeeder, 
        LogAndPengumumanSeeder logAndPengumumanSeeder
    ) {
        this.roleSeeder = roleSeeder;
        this.jurusanSeeder = jurusanSeeder;
        this.userAndBiodataSeeder = userAndBiodataSeeder;
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
        
        // Panggil setiap seeder secara berurutan untuk menjaga integritas data
        roleSeeder.seed();
        jurusanSeeder.seed();
        userAndBiodataSeeder.seed();
        akademikSeeder.seed(userAndBiodataSeeder.getCreatedDosens(), userAndBiodataSeeder.getCreatedMahasiswas());
        keuanganSeeder.seed(userAndBiodataSeeder.getCreatedMahasiswas());
        logAndPengumumanSeeder.seed(userAndBiodataSeeder.getAdminUser());

        System.out.println("Seeder selesai dijalankan.");
    }
}