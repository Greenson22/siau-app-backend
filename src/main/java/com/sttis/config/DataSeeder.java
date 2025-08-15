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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class DataSeeder implements CommandLineRunner {

    // --- REPOSITORIES ---
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
    private final PaketMatakuliahRepository paketMatakuliahRepository;
    private final DetailPaketMatakuliahRepository detailPaketMatakuliahRepository;


    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository,
                      JurusanRepository jurusanRepository, MataKuliahRepository mataKuliahRepository,
                      KelasRepository kelasRepository, KrsRepository krsRepository,
                      PresensiMahasiswaRepository presensiMahasiswaRepository, JadwalUjianRepository jadwalUjianRepository,
                      KomponenBiayaRepository komponenBiayaRepository, TagihanMahasiswaRepository tagihanMahasiswaRepository,
                      DetailTagihanRepository detailTagihanRepository, PembayaranRepository pembayaranRepository,
                      PengumumanRepository pengumumanRepository, BiodataMahasiswaRepository biodataMahasiswaRepository,
                      BiodataDosenRepository biodataDosenRepository, ActivityLogRepository activityLogRepository,
                      PasswordEncoder passwordEncoder, PaketMatakuliahRepository paketMatakuliahRepository,
                      DetailPaketMatakuliahRepository detailPaketMatakuliahRepository) {
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
        this.paketMatakuliahRepository = paketMatakuliahRepository;
        this.detailPaketMatakuliahRepository = detailPaketMatakuliahRepository;
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
        Jurusan teologi = new Jurusan(); teologi.setNamaJurusan("S1 Teologi"); teologi.setFakultas("Fakultas Teologi"); jurusanRepository.save(teologi);
        Jurusan pak = new Jurusan(); pak.setNamaJurusan("S1 Pendidikan Agama Kristen"); pak.setFakultas("Fakultas Pendidikan"); jurusanRepository.save(pak);
        
        // 3. Buat User Admin & Dosen Demo
        User adminUser = new User(); adminUser.setUsername("admin"); adminUser.setPassword(passwordEncoder.encode("admin")); adminUser.setRole(adminRole);
        userRepository.save(adminUser);

        User dosenDemoUser = new User(); dosenDemoUser.setUsername("dosen"); dosenDemoUser.setPassword(passwordEncoder.encode("dosen")); dosenDemoUser.setRole(dosenRole);
        userRepository.save(dosenDemoUser);

        // 4. Buat Dosen & Biodata Dosen
        List<Dosen> createdDosens = new ArrayList<>();
        Dosen dosenPA_Demo = new Dosen();
        dosenPA_Demo.setUser(dosenDemoUser);
        dosenPA_Demo.setNamaLengkap("Dr. Glenn Maramis, S.Kom., M.CompSc");
        dosenPA_Demo.setNidn("0912048801");
        dosenPA_Demo.setJurusan(teologi);
        dosenRepository.save(dosenPA_Demo);
        createdDosens.add(dosenPA_Demo);

        for (int i = 0; i < 5; i++) {
            User dosenUser = new User(); dosenUser.setUsername(faker.name().username()); dosenUser.setPassword(passwordEncoder.encode("dosen123")); dosenUser.setRole(dosenRole);
            User savedDosenUser = userRepository.save(dosenUser);
            
            Dosen dosen = new Dosen(); dosen.setUser(savedDosenUser); dosen.setNamaLengkap(faker.name().fullName()); dosen.setNidn(faker.number().digits(10)); dosen.setJurusan(i % 2 == 0 ? teologi : pak);
            Dosen savedDosen = dosenRepository.save(dosen);
            createdDosens.add(savedDosen);

            BiodataDosen biodataDosen = new BiodataDosen(); biodataDosen.setDosen(savedDosen); biodataDosen.setAlamat(faker.address().fullAddress()); biodataDosen.setNomorTelepon(faker.phoneNumber().phoneNumber()); biodataDosen.setSpesialisasi(faker.job().field());
            biodataDosenRepository.save(biodataDosen);
        }

        // 5. Buat Mahasiswa (Termasuk Mahasiswa Demo) & Biodata
        List<Mahasiswa> createdMahasiswas = new ArrayList<>();
        // Mahasiswa Demo
        User mhsDemoUser = new User(); mhsDemoUser.setUsername("20210118"); mhsDemoUser.setPassword(passwordEncoder.encode("password")); mhsDemoUser.setRole(mahasiswaRole);
        userRepository.save(mhsDemoUser);
        Mahasiswa mhsDemo = new Mahasiswa();
        mhsDemo.setUser(mhsDemoUser);
        mhsDemo.setNamaLengkap("Frendy Gerung");
        mhsDemo.setNim("20210118");
        mhsDemo.setJurusan(teologi);
        mhsDemo.setStatus(StatusMahasiswa.AKTIF);
        mhsDemo.setPembimbingAkademik(dosenPA_Demo); // Set Dosen PA
        mahasiswaRepository.save(mhsDemo);
        createdMahasiswas.add(mhsDemo);
        BiodataMahasiswa biodataMhsDemo = new BiodataMahasiswa(); biodataMhsDemo.setMahasiswa(mhsDemo); biodataMhsDemo.setAlamat("Jl. Raya Tahuna-Manganitu, Siau Timur"); biodataMhsDemo.setNomorTelepon("085298937694"); biodataMhsDemo.setTempatLahir("Manado"); biodataMhsDemo.setTanggalLahir(LocalDate.of(2003, 8, 17)); biodataMhsDemo.setJenisKelamin("Laki-laki");
        biodataMahasiswaRepository.save(biodataMhsDemo);


        // Mahasiswa lainnya
        for (int i = 0; i < 10; i++) {
            User mhsUser = new User(); mhsUser.setUsername(faker.name().username()); mhsUser.setPassword(passwordEncoder.encode("mahasiswa123")); mhsUser.setRole(mahasiswaRole);
            User savedDosenUser = userRepository.save(mhsUser);

            Mahasiswa mahasiswa = new Mahasiswa(); mahasiswa.setUser(savedDosenUser); mahasiswa.setNamaLengkap(faker.name().fullName()); mahasiswa.setNim("2025" + faker.number().digits(4)); mahasiswa.setJurusan(i % 2 == 0 ? teologi : pak); mahasiswa.setStatus(StatusMahasiswa.AKTIF);
            mahasiswa.setPembimbingAkademik(createdDosens.get(i % createdDosens.size())); // Assign Dosen PA
            Mahasiswa savedMhs = mahasiswaRepository.save(mahasiswa);
            createdMahasiswas.add(savedMhs);

            BiodataMahasiswa biodataMhs = new BiodataMahasiswa(); biodataMhs.setMahasiswa(savedMhs); biodataMhs.setAlamat(faker.address().fullAddress()); biodataMhs.setNomorTelepon(faker.phoneNumber().phoneNumber()); biodataMhs.setTempatLahir(faker.address().city()); biodataMhs.setTanggalLahir(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()); biodataMhs.setJenisKelamin(faker.options().option("Laki-laki", "Perempuan")); 
            biodataMahasiswaRepository.save(biodataMhs);
        }

        // 6. Buat Semua Mata Kuliah dan Paketnya
        List<MataKuliah> semuaMatkulTeologi = new ArrayList<>();
        // Semester 1
        MataKuliah teo101 = createMatkul("TEO101", "Pengantar Perjanjian Lama", 3, teologi);
        MataKuliah teo102 = createMatkul("TEO102", "Pengantar Perjanjian Baru", 3, teologi);
        MataKuliah pak101 = createMatkul("PAK101", "Dasar-Dasar PAK", 2, teologi);
        createPaket(teologi, 1, "2021/2022", Arrays.asList(teo101, teo102, pak101));
        semuaMatkulTeologi.addAll(Arrays.asList(teo101, teo102, pak101));

        // Semester 2
        MataKuliah teo201 = createMatkul("TEO201", "Teologi Sistematika I", 3, teologi);
        MataKuliah bib201 = createMatkul("BIB201", "Bahasa Yunani I", 2, teologi);
        MataKuliah mis201 = createMatkul("MIS201", "Misiologi I", 2, teologi);
        createPaket(teologi, 2, "2021/2022", Arrays.asList(teo201, bib201, mis201));
        semuaMatkulTeologi.addAll(Arrays.asList(teo201, bib201, mis201));

        // Semester 3
        MataKuliah teo301 = createMatkul("TEO301", "Teologi Sistematika II", 3, teologi);
        MataKuliah bib301 = createMatkul("BIB301", "Bahasa Ibrani I", 2, teologi);
        MataKuliah sej301 = createMatkul("SEJ301", "Sejarah Gereja Awal", 3, teologi);
        createPaket(teologi, 3, "2022/2023", Arrays.asList(teo301, bib301, sej301));
        semuaMatkulTeologi.addAll(Arrays.asList(teo301, bib301, sej301));

        // Semester 4
        MataKuliah teo402 = createMatkul("TEO402", "Hermeneutik", 3, teologi);
        MataKuliah pak402 = createMatkul("PAK402", "Psikologi Perkembangan", 3, teologi);
        MataKuliah pra402 = createMatkul("PRA402", "Homiletika I", 2, teologi);
        createPaket(teologi, 4, "2022/2023", Arrays.asList(teo402, pak402, pra402));
        semuaMatkulTeologi.addAll(Arrays.asList(teo402, pak402, pra402));
        
        // Semester 5
        MataKuliah teo501 = createMatkul("TEO501", "Etika Kristen", 3, teologi);
        MataKuliah bib501 = createMatkul("BIB501", "Eksegesis Perjanjian Lama", 3, teologi);
        MataKuliah mis501 = createMatkul("MIS501", "Apologetika", 2, teologi);
        createPaket(teologi, 5, "2023/2024", Arrays.asList(teo501, bib501, mis501));
        semuaMatkulTeologi.addAll(Arrays.asList(teo501, bib501, mis501));

        // Semester 6
        MataKuliah teo601 = createMatkul("TEO601", "Dogmatika", 3, teologi);
        MataKuliah bib601 = createMatkul("BIB601", "Eksegesis Perjanjian Baru", 3, teologi);
        MataKuliah pra601 = createMatkul("PRA601", "Pastoral Konseling", 2, teologi);
        createPaket(teologi, 6, "2023/2024", Arrays.asList(teo601, bib601, pra601));
        semuaMatkulTeologi.addAll(Arrays.asList(teo601, bib601, pra601));

        // Semester 7 (yang akan diambil)
        MataKuliah teo701 = createMatkul("TEO701", "Teologi Kontemporer", 3, teologi);
        MataKuliah pak701 = createMatkul("PAK701", "Pendidikan Kristen Lanjutan", 3, teologi);
        MataKuliah bib701 = createMatkul("BIB701", "Eksegesis Lanjutan", 3, teologi);
        MataKuliah pra701 = createMatkul("PRA701", "Praktik Pelayanan Lanjutan", 2, teologi);
        MataKuliah sej701 = createMatkul("SEJ701", "Sejarah Gereja Modern", 2, teologi);
        createPaket(teologi, 7, "2024/2025", Arrays.asList(teo701, pak701, bib701, pra701, sej701));
        semuaMatkulTeologi.addAll(Arrays.asList(teo701, pak701, bib701, pra701, sej701));
        
        // 7. Buat Riwayat KRS untuk Mahasiswa Demo (Semester 1-6)
        // Ini penting agar sistem bisa menghitung semester aktifnya adalah 7
        List<MataKuliah> historyMatkul = semuaMatkulTeologi.subList(0, semuaMatkulTeologi.indexOf(teo701));
        for(MataKuliah mk : historyMatkul) {
            Kelas kelasHistori = new Kelas();
            kelasHistori.setMataKuliah(mk);
            kelasHistori.setDosen(createdDosens.get(faker.number().numberBetween(0, createdDosens.size())));
            kelasHistori.setTahunAkademik("HISTORI"); // Tandai sebagai histori
            kelasHistori.setSemester(Semester.GANJIL); // Tidak relevan, tapi harus diisi
            kelasRepository.save(kelasHistori);

            Krs krsHistori = new Krs();
            krsHistori.setMahasiswa(mhsDemo);
            krsHistori.setKelas(kelasHistori);
            krsHistori.setStatusPersetujuan(StatusPersetujuan.DISETUJUI);
            krsHistori.setNilaiHuruf(faker.options().option("A", "A-", "B+", "B"));
            krsHistori.setNilaiAkhir(new BigDecimal(faker.number().randomDouble(2, 80, 95)));
            krsRepository.save(krsHistori);
        }
        
        // 8. Buat Kelas & Jadwal Ujian (Hanya untuk Semester 7)
        for(MataKuliah mk : semuaMatkulTeologi.subList(semuaMatkulTeologi.indexOf(teo701), semuaMatkulTeologi.size())) {
             Kelas kelas = new Kelas();
            kelas.setMataKuliah(mk);
            kelas.setDosen(createdDosens.get(faker.number().numberBetween(0, createdDosens.size())));
            kelas.setTahunAkademik("2024/2025");
            kelas.setSemester(Semester.GANJIL);
            kelas.setHari(faker.options().option("Senin", "Selasa", "Rabu", "Kamis", "Jumat"));
            kelas.setJamMulai(LocalTime.of(faker.number().numberBetween(8, 15), 0));
            kelas.setRuangan("R." + faker.number().numberBetween(101, 305));
            kelasRepository.save(kelas);
        }
        
        // 9. Buat Keuangan
        KomponenBiaya spp = new KomponenBiaya(); spp.setNamaKomponen("Biaya Kuliah Bulanan"); spp.setJenisBiaya(JenisBiaya.BULANAN); komponenBiayaRepository.save(spp);
        KomponenBiaya pembangunan = new KomponenBiaya(); pembangunan.setNamaKomponen("Uang Pembangunan"); pembangunan.setJenisBiaya(JenisBiaya.SEKALI_BAYAR); komponenBiayaRepository.save(pembangunan);

        for (Mahasiswa mhs : createdMahasiswas) {
            TagihanMahasiswa tagihan = new TagihanMahasiswa(); tagihan.setMahasiswa(mhs); tagihan.setDeskripsiTagihan("Tagihan Semester Ganjil 2024/2025"); tagihan.setTanggalJatuhTempo(LocalDate.now().plusMonths(1)); tagihan.setStatus(StatusTagihan.BELUM_LUNAS); tagihan.setTotalTagihan(new BigDecimal("3500000"));
            TagihanMahasiswa savedTagihan = tagihanMahasiswaRepository.save(tagihan);

            DetailTagihan detail = new DetailTagihan(); detail.setTagihanMahasiswa(savedTagihan); detail.setKomponenBiaya(spp); detail.setJumlah(new BigDecimal("3500000"));
            detailTagihanRepository.save(detail);

            if (mhs.equals(mhsDemo) || faker.bool().bool()) { // Pastikan mhsDemo lunas
                Pembayaran bayar = new Pembayaran(); bayar.setTagihan(savedTagihan); bayar.setJumlahBayar(new BigDecimal("3500000")); bayar.setTanggalBayar(java.time.LocalDateTime.now()); bayar.setMetodePembayaran("Transfer Bank"); bayar.setStatusVerifikasi(StatusVerifikasi.BERHASIL);
                pembayaranRepository.save(bayar);
                savedTagihan.setStatus(StatusTagihan.LUNAS);
                tagihanMahasiswaRepository.save(savedTagihan);
            }
        }
        
        // 10. Buat Pengumuman
        Pengumuman pengumuman1 = new Pengumuman(); pengumuman1.setJudul("Batas Akhir Pembayaran UKT"); pengumuman1.setIsi("Batas akhir pembayaran UKT untuk semester Ganjil 2024/2025 adalah tanggal 30 September 2024. Harap segera melakukan pembayaran."); pengumuman1.setPembuat(adminUser); pengumuman1.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(1));
        pengumumanRepository.save(pengumuman1);
        Pengumuman pengumuman2 = new Pengumuman(); pengumuman2.setJudul("Validasi KRS oleh Dosen PA"); pengumuman2.setIsi("Periode validasi KRS oleh Dosen Pembimbing Akademik akan dilaksanakan mulai tanggal 25 hingga 31 Agustus 2024."); pengumuman2.setPembuat(adminUser); pengumuman2.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(2));
        pengumumanRepository.save(pengumuman2);
        
        // 11. Buat Activity Log
        ActivityLog log = new ActivityLog(); log.setUser(adminUser); log.setAksi("SEED_DATABASE"); log.setDeskripsi("Semua data dummy berhasil dibuat."); log.setTimestamp(java.time.LocalDateTime.now());
        activityLogRepository.save(log);

        System.out.println("Seeder selesai dijalankan.");
    }

    // Helper method untuk membuat MataKuliah
    private MataKuliah createMatkul(String kode, String nama, int sks, Jurusan jurusan) {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMatkul(kode);
        mk.setNamaMatkul(nama);
        mk.setSks(sks);
        mk.setJurusan(jurusan);
        return mataKuliahRepository.save(mk);
    }

    // Helper method untuk membuat PaketMatakuliah
    private void createPaket(Jurusan jurusan, int semester, String tahun, List<MataKuliah> matkulList) {
        PaketMatakuliah paket = new PaketMatakuliah();
        paket.setNamaPaket("Paket Matakuliah " + jurusan.getNamaJurusan() + " Semester " + semester);
        paket.setJurusan(jurusan);
        paket.setSemesterKe(semester);
        paket.setTahunAkademik(tahun);
        paketMatakuliahRepository.save(paket);

        for (MataKuliah mk : matkulList) {
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(paket);
            detail.setMataKuliah(mk);
            detailPaketMatakuliahRepository.save(detail);
        }
    }
}