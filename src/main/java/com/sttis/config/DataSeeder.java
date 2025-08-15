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
    // --- REPOSITORIES BARU ---
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
            User savedMhsUser = userRepository.save(mhsUser);

            Mahasiswa mahasiswa = new Mahasiswa(); mahasiswa.setUser(savedMhsUser); mahasiswa.setNamaLengkap(faker.name().fullName()); mahasiswa.setNim("2025" + faker.number().digits(4)); mahasiswa.setJurusan(i % 2 == 0 ? teologi : pak); mahasiswa.setStatus(StatusMahasiswa.AKTIF);
            mahasiswa.setPembimbingAkademik(createdDosens.get(i % createdDosens.size())); // Assign Dosen PA
            Mahasiswa savedMhs = mahasiswaRepository.save(mahasiswa);
            createdMahasiswas.add(savedMhs);

            BiodataMahasiswa biodataMhs = new BiodataMahasiswa(); biodataMhs.setMahasiswa(savedMhs); biodataMhs.setAlamat(faker.address().fullAddress()); biodataMhs.setNomorTelepon(faker.phoneNumber().phoneNumber()); biodataMhs.setTempatLahir(faker.address().city()); biodataMhs.setTanggalLahir(faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()); biodataMhs.setJenisKelamin(faker.options().option("Laki-laki", "Perempuan")); 
            biodataMahasiswaRepository.save(biodataMhs);
        }

        // 6. Buat Mata Kuliah
        MataKuliah teo401 = new MataKuliah(); teo401.setKodeMatkul("TEO401"); teo401.setNamaMatkul("Teologi Kontemporer"); teo401.setSks(3); teo401.setJurusan(teologi); mataKuliahRepository.save(teo401);
        MataKuliah pak401 = new MataKuliah(); pak401.setKodeMatkul("PAK401"); pak401.setNamaMatkul("Pendidikan Kristen Lanjutan"); pak401.setSks(3); pak401.setJurusan(teologi); mataKuliahRepository.save(pak401);
        MataKuliah bib401 = new MataKuliah(); bib401.setKodeMatkul("BIB401"); bib401.setNamaMatkul("Eksegesis Lanjutan"); bib401.setSks(3); bib401.setJurusan(teologi); mataKuliahRepository.save(bib401);
        MataKuliah pra401 = new MataKuliah(); pra401.setKodeMatkul("PRA401"); pra401.setNamaMatkul("Praktik Pelayanan Lanjutan"); pra401.setSks(2); pra401.setJurusan(teologi); mataKuliahRepository.save(pra401);
        MataKuliah sej401 = new MataKuliah(); sej401.setKodeMatkul("SEJ401"); sej401.setNamaMatkul("Sejarah Gereja Modern"); sej401.setSks(2); sej401.setJurusan(teologi); mataKuliahRepository.save(sej401);
        List<MataKuliah> matkulTeologiSm7 = List.of(teo401, pak401, bib401, pra401, sej401);
        
        // 7. Buat Paket Matakuliah
        PaketMatakuliah paketTeologiSm7 = new PaketMatakuliah();
        paketTeologiSm7.setNamaPaket("Paket Matakuliah Teologi Semester 7");
        paketTeologiSm7.setJurusan(teologi);
        paketTeologiSm7.setSemesterKe(7);
        paketTeologiSm7.setTahunAkademik("2024/2025");
        paketMatakuliahRepository.save(paketTeologiSm7);

        for(MataKuliah mk : matkulTeologiSm7) {
            DetailPaketMatakuliah detail = new DetailPaketMatakuliah();
            detail.setPaketMatakuliah(paketTeologiSm7);
            detail.setMataKuliah(mk);
            detailPaketMatakuliahRepository.save(detail);
        }

        // 8. Buat Kelas & Jadwal Ujian
        List<Kelas> createdKelas = new ArrayList<>();
        for(MataKuliah mk : matkulTeologiSm7) {
            Kelas kelas = new Kelas();
            kelas.setMataKuliah(mk);
            kelas.setDosen(createdDosens.get(faker.number().numberBetween(0, createdDosens.size())));
            kelas.setTahunAkademik("2024/2025");
            kelas.setSemester(Semester.GANJIL);
            kelas.setHari(faker.options().option("Senin", "Selasa", "Rabu", "Kamis", "Jumat"));
            kelas.setJamMulai(LocalTime.of(faker.number().numberBetween(8, 15), 0));
            kelas.setRuangan("R." + faker.number().numberBetween(101, 305));
            Kelas savedKelas = kelasRepository.save(kelas);
            createdKelas.add(savedKelas);
            
            JadwalUjian ujian = new JadwalUjian(); ujian.setKelas(savedKelas); ujian.setJenisUjian(JenisUjian.UAS); ujian.setTanggalUjian(LocalDate.now().plusMonths(3)); ujian.setJamMulai(LocalTime.of(8,0)); ujian.setJamSelesai(LocalTime.of(10,0)); ujian.setRuangan("Aula");
            jadwalUjianRepository.save(ujian);
        }

        // 9. Buat KRS & Presensi
        // KRS untuk mahasiswa demo (berdasarkan paket)
        for(Kelas k : createdKelas){
            Krs krs = new Krs();
            krs.setMahasiswa(mhsDemo);
            krs.setKelas(k);
            krs.setStatusPersetujuan(StatusPersetujuan.DISETUJUI); // Anggap sudah disetujui
            krs.setNilaiHuruf("A"); // Beri nilai dummy
            krs.setNilaiAkhir(new BigDecimal("95.00"));
            Krs savedKrs = krsRepository.save(krs);

            for (int i = 1; i <= 14; i++) { // Buat 14 pertemuan
                PresensiMahasiswa presensi = new PresensiMahasiswa();
                presensi.setKrs(savedKrs);
                presensi.setPertemuanKe(i);
                presensi.setStatusHadir(faker.options().option(StatusHadir.HADIR, StatusHadir.HADIR, StatusHadir.SAKIT, StatusHadir.IZIN, StatusHadir.ALFA));
                presensi.setTanggal(LocalDate.now().minusDays(90).plusDays(i*7));
                presensiMahasiswaRepository.save(presensi);
            }
        }
        
        // 10. Buat Keuangan
        KomponenBiaya spp = new KomponenBiaya(); spp.setNamaKomponen("Biaya Kuliah Bulanan"); spp.setJenisBiaya(JenisBiaya.BULANAN); komponenBiayaRepository.save(spp);
        KomponenBiaya pembangunan = new KomponenBiaya(); pembangunan.setNamaKomponen("Uang Pembangunan"); pembangunan.setJenisBiaya(JenisBiaya.SEKALI_BAYAR); komponenBiayaRepository.save(pembangunan);

        for (Mahasiswa mhs : createdMahasiswas) {
            TagihanMahasiswa tagihan = new TagihanMahasiswa(); tagihan.setMahasiswa(mhs); tagihan.setDeskripsiTagihan("Tagihan Semester Ganjil 2024/2025"); tagihan.setTanggalJatuhTempo(LocalDate.now().plusMonths(1)); tagihan.setStatus(StatusTagihan.BELUM_LUNAS); tagihan.setTotalTagihan(new BigDecimal("3500000"));
            TagihanMahasiswa savedTagihan = tagihanMahasiswaRepository.save(tagihan);

            DetailTagihan detail = new DetailTagihan(); detail.setTagihanMahasiswa(savedTagihan); detail.setKomponenBiaya(spp); detail.setJumlah(new BigDecimal("3500000"));
            detailTagihanRepository.save(detail);

            if (faker.bool().bool()) {
                Pembayaran bayar = new Pembayaran(); bayar.setTagihan(savedTagihan); bayar.setJumlahBayar(new BigDecimal("3500000")); bayar.setTanggalBayar(java.time.LocalDateTime.now()); bayar.setMetodePembayaran("Transfer Bank"); bayar.setStatusVerifikasi(StatusVerifikasi.BERHASIL);
                pembayaranRepository.save(bayar);
                savedTagihan.setStatus(StatusTagihan.LUNAS);
                tagihanMahasiswaRepository.save(savedTagihan);
            }
        }
        
        // 11. Buat Pengumuman
        Pengumuman pengumuman1 = new Pengumuman(); pengumuman1.setJudul("Batas Akhir Pembayaran UKT"); pengumuman1.setIsi("Batas akhir pembayaran UKT untuk semester Ganjil 2024/2025 adalah tanggal 30 September 2024. Harap segera melakukan pembayaran."); pengumuman1.setPembuat(adminUser); pengumuman1.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(1));
        pengumumanRepository.save(pengumuman1);
        Pengumuman pengumuman2 = new Pengumuman(); pengumuman2.setJudul("Validasi KRS oleh Dosen PA"); pengumuman2.setIsi("Periode validasi KRS oleh Dosen Pembimbing Akademik akan dilaksanakan mulai tanggal 25 hingga 31 Agustus 2024."); pengumuman2.setPembuat(adminUser); pengumuman2.setTanggalTerbit(java.time.LocalDateTime.now().minusDays(2));
        pengumumanRepository.save(pengumuman2);
        
        // 12. Buat Activity Log
        ActivityLog log = new ActivityLog(); log.setUser(adminUser); log.setAksi("SEED_DATABASE"); log.setDeskripsi("Semua data dummy berhasil dibuat."); log.setTimestamp(java.time.LocalDateTime.now());
        activityLogRepository.save(log);

        System.out.println("Seeder selesai dijalankan.");
    }
}