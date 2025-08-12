package com.sttis.config;

import com.github.javafaker.Faker;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.*;
import com.sttis.models.repos.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DataSeeder implements CommandLineRunner {

    // --- SEMUA DEPENDENSI REPOSITORY ---
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;
    private final JurusanRepository jurusanRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final KelasRepository kelasRepository;
    private final KrsRepository krsRepository;
    private final PresensiMahasiswaRepository presensiMahasiswaRepository;
    private final JadwalUjianRepository jadwalUjianRepository;
    private final KomponenBiayaRepository komponenBiayaRepository;
    private final TagihanMahasiswaRepository tagihanMahasiswaRepository;
    private final DetailTagihanRepository detailTagihanRepository;
    private final PembayaranRepository pembayaranRepository;
    private final PengumumanRepository pengumumanRepository;
    private final BiodataMahasiswaRepository biodataMahasiswaRepository;
    private final BiodataDosenRepository biodataDosenRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;

    // --- CONSTRUCTOR DENGAN SEMUA DEPENDENSI ---
    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository,
                      JurusanRepository jurusanRepository, MataKuliahRepository mataKuliahRepository,
                      KelasRepository kelasRepository, KrsRepository krsRepository,
                      PresensiMahasiswaRepository presensiMahasiswaRepository, JadwalUjianRepository jadwalUjianRepository,
                      KomponenBiayaRepository komponenBiayaRepository, TagihanMahasiswaRepository tagihanMahasiswaRepository,
                      DetailTagihanRepository detailTagihanRepository, PembayaranRepository pembayaranRepository,
                      PengumumanRepository pengumumanRepository, BiodataMahasiswaRepository biodataMahasiswaRepository,
                      BiodataDosenRepository biodataDosenRepository, ActivityLogRepository activityLogRepository,
                      PasswordEncoder passwordEncoder) {
        // Inisialisasi semua repository
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
        this.jurusanRepository = jurusanRepository;
        this.mataKuliahRepository = mataKuliahRepository;
        this.kelasRepository = kelasRepository;
        this.krsRepository = krsRepository;
        this.presensiMahasiswaRepository = presensiMahasiswaRepository;
        this.jadwalUjianRepository = jadwalUjianRepository;
        this.komponenBiayaRepository = komponenBiayaRepository;
        this.tagihanMahasiswaRepository = tagihanMahasiswaRepository;
        this.detailTagihanRepository = detailTagihanRepository;
        this.pembayaranRepository = pembayaranRepository;
        this.pengumumanRepository = pengumumanRepository;
        this.biodataMahasiswaRepository = biodataMahasiswaRepository;
        this.biodataDosenRepository = biodataDosenRepository;
        this.activityLogRepository = activityLogRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() > 0) {
            System.out.println("Data sudah ada, seeder tidak dijalankan.");
            return;
        }

        System.out.println("Menjalankan Seeder untuk semua tabel...");
        Faker faker = new Faker(new Locale("id-ID"));

        // 1. Buat Roles
        Role adminRole = new Role(); adminRole.setRoleName("Admin"); roleRepository.save(adminRole);
        Role dosenRole = new Role(); dosenRole.setRoleName("Dosen"); roleRepository.save(dosenRole);
        Role mahasiswaRole = new Role(); mahasiswaRole.setRoleName("Mahasiswa"); roleRepository.save(mahasiswaRole);

        // 2. Buat Jurusan
        Jurusan ti = new Jurusan(); ti.setNamaJurusan("Teknik Informatika"); ti.setFakultas("Fakultas Teknologi Informasi"); jurusanRepository.save(ti);
        Jurusan si = new Jurusan(); si.setNamaJurusan("Sistem Informasi"); si.setFakultas("Fakultas Teknologi Informasi"); jurusanRepository.save(si);
        
        // 3. Buat User Admin
        User adminUser = new User(); adminUser.setUsername("admin"); adminUser.setPassword(passwordEncoder.encode("admin123")); adminUser.setRole(adminRole);
        userRepository.save(adminUser);

        // 4. Buat Dosen & Biodata Dosen
        List<Dosen> createdDosens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User dosenUser = new User(); dosenUser.setUsername(faker.name().username()); dosenUser.setPassword(passwordEncoder.encode("dosen123")); dosenUser.setRole(dosenRole);
            User savedDosenUser = userRepository.save(dosenUser);
            
            Dosen dosen = new Dosen(); dosen.setUser(savedDosenUser); dosen.setNamaLengkap(faker.name().fullName()); dosen.setNidn(faker.number().digits(10)); dosen.setJurusan(ti);
            Dosen savedDosen = dosenRepository.save(dosen);
            createdDosens.add(savedDosen);

            BiodataDosen biodataDosen = new BiodataDosen(); biodataDosen.setDosen(savedDosen); biodataDosen.setAlamat(faker.address().fullAddress()); biodataDosen.setNomorTelepon(faker.phoneNumber().phoneNumber()); biodataDosen.setSpesialisasi(faker.job().field());
            biodataDosenRepository.save(biodataDosen);
        }

        // 5. Buat Mahasiswa & Biodata Mahasiswa
        List<Mahasiswa> createdMahasiswas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User mhsUser = new User(); mhsUser.setUsername(faker.name().username()); mhsUser.setPassword(passwordEncoder.encode("mahasiswa123")); mhsUser.setRole(mahasiswaRole);
            User savedMhsUser = userRepository.save(mhsUser);

            Mahasiswa mahasiswa = new Mahasiswa(); mahasiswa.setUser(savedMhsUser); mahasiswa.setNamaLengkap(faker.name().fullName()); mahasiswa.setNim("2025" + faker.number().digits(4)); mahasiswa.setJurusan(i % 2 == 0 ? ti : si); mahasiswa.setStatus(StatusMahasiswa.AKTIF);
            Mahasiswa savedMhs = mahasiswaRepository.save(mahasiswa);
            createdMahasiswas.add(savedMhs);

            BiodataMahasiswa biodataMhs = new BiodataMahasiswa(); biodataMhs.setMahasiswa(savedMhs); biodataMhs.setAlamat(faker.address().fullAddress()); biodataMhs.setNomorTelepon(faker.phoneNumber().phoneNumber()); biodataMhs.setTempatLahir(faker.address().city()); biodataMhs.setTanggalLahir(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()); biodataMhs.setJenisKelamin(faker.options().option("Laki-laki", "Perempuan")); 
            biodataMahasiswaRepository.save(biodataMhs);
        }

        // 6. Buat Mata Kuliah
        MataKuliah daspro = new MataKuliah(); daspro.setKodeMatkul("IF101"); daspro.setNamaMatkul("Dasar Pemrograman"); daspro.setSks(3); daspro.setJurusan(ti); mataKuliahRepository.save(daspro);
        MataKuliah pbo = new MataKuliah(); pbo.setKodeMatkul("IF102"); pbo.setNamaMatkul("Pemrograman Berorientasi Objek"); pbo.setSks(3); pbo.setJurusan(ti); mataKuliahRepository.save(pbo);
        MataKuliah basisData = new MataKuliah(); basisData.setKodeMatkul("SI101"); basisData.setNamaMatkul("Basis Data"); basisData.setSks(3); basisData.setJurusan(si); mataKuliahRepository.save(basisData);
        List<MataKuliah> allMatkul = List.of(daspro, pbo, basisData);

        // 7. Buat Kelas & Jadwal Ujian
        List<Kelas> createdKelas = new ArrayList<>();
        for(MataKuliah mk : allMatkul) {
            Kelas kelas = new Kelas(); kelas.setMataKuliah(mk); kelas.setDosen(createdDosens.get(faker.number().numberBetween(0, createdDosens.size()))); kelas.setTahunAkademik("2025/2026"); kelas.setSemester(Semester.GANJIL); kelas.setHari("Senin"); kelas.setJamMulai(LocalTime.of(8, 0)); kelas.setRuangan("R.301");
            Kelas savedKelas = kelasRepository.save(kelas);
            createdKelas.add(savedKelas);
            
            JadwalUjian ujian = new JadwalUjian(); ujian.setKelas(savedKelas); ujian.setJenisUjian(JenisUjian.UTS); ujian.setTanggalUjian(LocalDate.now().plusMonths(2)); ujian.setJamMulai(LocalTime.of(8,0)); ujian.setJamSelesai(LocalTime.of(10,0)); ujian.setRuangan("Aula");
            jadwalUjianRepository.save(ujian);
        }

        // 8. Buat KRS & Presensi
        for(Mahasiswa mhs : createdMahasiswas) {
            Kelas kelasDiambil = createdKelas.get(faker.number().numberBetween(0, createdKelas.size()));
            Krs krs = new Krs(); krs.setMahasiswa(mhs); krs.setKelas(kelasDiambil); krs.setStatusPersetujuan(StatusPersetujuan.DISETUJUI);
            Krs savedKrs = krsRepository.save(krs);

            for (int i = 1; i <= 5; i++) {
                PresensiMahasiswa presensi = new PresensiMahasiswa(); presensi.setKrs(savedKrs); presensi.setPertemuanKe(i); presensi.setStatusHadir(StatusHadir.HADIR); presensi.setTanggal(LocalDate.now().plusDays(i*7));
                presensiMahasiswaRepository.save(presensi);
            }
        }
        
        // 9. Buat Keuangan
        KomponenBiaya spp = new KomponenBiaya(); spp.setNamaKomponen("Biaya Kuliah Bulanan"); spp.setJenisBiaya(JenisBiaya.BULANAN); komponenBiayaRepository.save(spp);
        KomponenBiaya pembangunan = new KomponenBiaya(); pembangunan.setNamaKomponen("Uang Pembangunan"); pembangunan.setJenisBiaya(JenisBiaya.SEKALI_BAYAR); komponenBiayaRepository.save(pembangunan);

        for (Mahasiswa mhs : createdMahasiswas) {
            TagihanMahasiswa tagihan = new TagihanMahasiswa(); tagihan.setMahasiswa(mhs); tagihan.setDeskripsiTagihan("Tagihan Agustus 2025"); tagihan.setTanggalJatuhTempo(LocalDate.now().plusMonths(1)); tagihan.setStatus(StatusTagihan.BELUM_LUNAS); tagihan.setTotalTagihan(new BigDecimal("2500000"));
            TagihanMahasiswa savedTagihan = tagihanMahasiswaRepository.save(tagihan);

            DetailTagihan detail = new DetailTagihan(); detail.setTagihanMahasiswa(savedTagihan); detail.setKomponenBiaya(spp); detail.setJumlah(new BigDecimal("2500000"));
            detailTagihanRepository.save(detail);

            if (faker.bool().bool()) { // Randomly make a payment
                Pembayaran bayar = new Pembayaran(); bayar.setTagihan(savedTagihan); bayar.setJumlahBayar(new BigDecimal("2500000")); bayar.setTanggalBayar(java.time.LocalDateTime.now()); bayar.setMetodePembayaran("Transfer Bank"); bayar.setStatusVerifikasi(StatusVerifikasi.BERHASIL);
                pembayaranRepository.save(bayar);
                savedTagihan.setStatus(StatusTagihan.LUNAS);
                tagihanMahasiswaRepository.save(savedTagihan);
            }
        }
        
        // 10. Buat Pengumuman
        Pengumuman pengumuman = new Pengumuman(); pengumuman.setJudul("Jadwal Libur Semester"); pengumuman.setIsi(faker.lorem().paragraph(5)); pengumuman.setPembuat(adminUser); pengumuman.setTanggalTerbit(java.time.LocalDateTime.now());
        pengumumanRepository.save(pengumuman);
        
        // 11. Buat Activity Log
        ActivityLog log = new ActivityLog(); log.setUser(adminUser); log.setAksi("SEED_DATABASE"); log.setDeskripsi("Semua data dummy berhasil dibuat."); log.setTimestamp(java.time.LocalDateTime.now());
        activityLogRepository.save(log);

        System.out.println("Seeder selesai dijalankan.");
    }
}