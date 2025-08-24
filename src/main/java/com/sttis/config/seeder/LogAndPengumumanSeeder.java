package com.sttis.config.seeder;

import com.sttis.models.entities.ActivityLog;
import com.sttis.models.entities.Pengumuman;
import com.sttis.models.entities.User;
import com.sttis.models.repos.ActivityLogRepository;
import com.sttis.models.repos.PengumumanRepository;
import org.springframework.stereotype.Component;

@Component
public class LogAndPengumumanSeeder {

    private final PengumumanRepository pengumumanRepository;
    private final ActivityLogRepository activityLogRepository;

    public LogAndPengumumanSeeder(PengumumanRepository pengumumanRepository, ActivityLogRepository activityLogRepository) {
        this.pengumumanRepository = pengumumanRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public void seed(User adminUser) {
        // 1. Create Pengumuman
        Pengumuman pengumuman1 = new Pengumuman();
        pengumuman1.setJudul("Batas Akhir Pembayaran UKT");
        pengumuman1.setIsi("Batas akhir pembayaran UKT untuk semester Ganjil 2024/2025 adalah tanggal 30 September 2024. Harap segera melakukan pembayaran.");
        pengumuman1.setPembuat(adminUser);
        pengumuman1.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(1));
        pengumumanRepository.save(pengumuman1);

        Pengumuman pengumuman2 = new Pengumuman();
        pengumuman2.setJudul("Validasi KRS oleh Dosen PA");
        pengumuman2.setIsi("Periode validasi KRS oleh Dosen Pembimbing Akademik akan dilaksanakan mulai tanggal 25 hingga 31 Agustus 2024.");
        pengumuman2.setPembuat(adminUser);
        pengumuman2.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(2));
        pengumumanRepository.save(pengumuman2);

        // 2. Create Initial Activity Log
        ActivityLog log = new ActivityLog();
        log.setUser(adminUser);
        log.setAksi("SEED_DATABASE");
        log.setDeskripsi("Semua data dummy berhasil dibuat.");
        log.setTimestamp(java.time.LocalDateTime.now());
        activityLogRepository.save(log);
        
        System.out.println("Seeder: Data Log & Pengumuman berhasil dibuat.");
    }
}