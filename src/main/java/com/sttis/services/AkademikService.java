package com.sttis.services;

import com.sttis.dto.AkademikSummaryDTO;
import com.sttis.dto.BiodataMahasiswaDTO;
import com.sttis.dto.DetailPresensiDTO;
import com.sttis.dto.IpsDataDTO;
import com.sttis.dto.KhsDTO;
import com.sttis.dto.KelasDTO;
import com.sttis.dto.RekapPresensiDTO;
import com.sttis.models.entities.BiodataMahasiswa;
import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.User;
import com.sttis.models.entities.enums.StatusPersetujuan;
import com.sttis.models.repos.KrsRepository;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true) // Gunakan readOnly untuk operasi GET
public class AkademikService {

    private final UserRepository userRepository;
    private final KrsRepository krsRepository;

    public AkademikService(UserRepository userRepository, KrsRepository krsRepository) {
        this.userRepository = userRepository;
        this.krsRepository = krsRepository;
    }

    /**
     * BARU: Mengambil jadwal kuliah mahasiswa berdasarkan KRS yang disetujui.
     */
    public List<KelasDTO> getMyJadwal(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                // Hanya ambil kelas dari KRS yang sudah disetujui
                .filter(krs -> krs.getStatusPersetujuan() == StatusPersetujuan.DISETUJUI)
                // Ambil objek Kelas dari setiap KRS
                .map(Krs::getKelas)
                // Konversi setiap Kelas menjadi KelasDTO
                .map(this::convertToKelasDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil Kartu Hasil Studi (KHS) untuk mahasiswa yang sedang login.
     * Hanya menampilkan mata kuliah yang sudah memiliki nilai.
     */
    public List<KhsDTO> getMyKhs(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                .filter(krs -> krs.getNilaiHuruf() != null && !krs.getNilaiHuruf().isBlank())
                .map(this::convertToKhsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil rekapitulasi presensi per kelas untuk mahasiswa yang sedang login.
     */
    public List<RekapPresensiDTO> getMyRekapPresensi(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        return krsRepository.findByMahasiswa(mahasiswa).stream()
                .map(this::convertToRekapPresensiDTO)
                .collect(Collectors.toList());
    }

    // Helper untuk mencari mahasiswa berdasarkan username
    private Mahasiswa getMahasiswaFromUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        if (user.getMahasiswa() == null) {
            throw new IllegalStateException("User ini bukan seorang mahasiswa.");
        }
        return user.getMahasiswa();
    }

    // Helper untuk konversi ke DTO
    private KhsDTO convertToKhsDTO(Krs krs) {
        KhsDTO dto = new KhsDTO();
        dto.setKodeMataKuliah(krs.getKelas().getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(krs.getKelas().getMataKuliah().getNamaMatkul());
        dto.setSks(krs.getKelas().getMataKuliah().getSks());
        dto.setTahunAkademik(krs.getKelas().getTahunAkademik());
        dto.setSemester(krs.getKelas().getSemester().name());
        dto.setNilaiAkhir(krs.getNilaiAkhir());
        dto.setNilaiHuruf(krs.getNilaiHuruf());
        return dto;
    }

    private RekapPresensiDTO convertToRekapPresensiDTO(Krs krs) {
        RekapPresensiDTO dto = new RekapPresensiDTO();
        dto.setKodeMataKuliah(krs.getKelas().getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(krs.getKelas().getMataKuliah().getNamaMatkul());
        dto.setNamaDosen(krs.getKelas().getDosen().getNamaLengkap());

        List<DetailPresensiDTO> detailList = krs.getPresensiList().stream().map(presensi -> {
            DetailPresensiDTO detail = new DetailPresensiDTO();
            detail.setPertemuanKe(presensi.getPertemuanKe());
            detail.setTanggal(presensi.getTanggal());
            detail.setStatusHadir(presensi.getStatusHadir().name());
            return detail;
        }).collect(Collectors.toList());

        dto.setDetailPresensi(detailList);
        return dto;
    }


    /**
     * ENDPOINT BARU: Mengambil biodata untuk mahasiswa yang sedang login.
     */
    public BiodataMahasiswaDTO getMyBiodata(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);
        BiodataMahasiswa biodata = mahasiswa.getBiodata();

        // Jika biodata belum ada, kembalikan objek kosong agar tidak error di frontend.
        if (biodata == null) {
            return new BiodataMahasiswaDTO();
        }
        return convertToBiodataDTO(biodata);
    }

        /**
     * BARU: Mengambil riwayat IPS per semester untuk grafik.
     */
    public IpsDataDTO getIpsHistory(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        // 1. Ambil semua KHS yang sudah dinilai
        List<Krs> krsBernilai = krsRepository.findByMahasiswa(mahasiswa).stream()
                .filter(krs -> krs.getNilaiHuruf() != null && !krs.getNilaiHuruf().isBlank())
                .collect(Collectors.toList());

        // 2. Kelompokkan KHS berdasarkan periode (Tahun Akademik + Semester)
        Map<String, List<Krs>> krsPerSemester = krsBernilai.stream()
                .collect(Collectors.groupingBy(krs ->
                        krs.getKelas().getTahunAkademik() + " " + krs.getKelas().getSemester()
                ));

        // 3. Urutkan semester berdasarkan tahun dan semester (Ganjil -> Genap)
        Map<String, List<Krs>> sortedKrsPerSemester = krsPerSemester.entrySet().stream()
                .sorted(Comparator.comparing(entry -> {
                    String[] parts = entry.getKey().split(" ");
                    String tahun = parts[0];
                    int semesterOrder = parts[1].equalsIgnoreCase("GANJIL") ? 1 : 2;
                    return tahun + "-" + semesterOrder;
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        // 4. Hitung IPS untuk setiap semester dan format untuk DTO
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        int semesterCounter = 1;

        for (Map.Entry<String, List<Krs>> entry : sortedKrsPerSemester.entrySet()) {
            labels.add("Sm " + semesterCounter++);
            
            BigDecimal totalBobotSks = BigDecimal.ZERO;
            int totalSks = 0;

            for (Krs krs : entry.getValue()) {
                int sks = krs.getKelas().getMataKuliah().getSks();
                BigDecimal bobot = getBobotNilai(krs.getNilaiHuruf());
                totalBobotSks = totalBobotSks.add(bobot.multiply(new BigDecimal(sks)));
                totalSks += sks;
            }

            BigDecimal ips = (totalSks > 0)
                    ? totalBobotSks.divide(new BigDecimal(totalSks), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            data.add(ips);
        }

        IpsDataDTO dto = new IpsDataDTO();
        dto.setLabels(labels);
        dto.setData(data);
        return dto;
    }

    /**
     * HELPER BARU: Konversi entitas BiodataMahasiswa ke DTO.
     */
    private BiodataMahasiswaDTO convertToBiodataDTO(BiodataMahasiswa biodata) {
        BiodataMahasiswaDTO dto = new BiodataMahasiswaDTO();
        dto.setTempatLahir(biodata.getTempatLahir());
        dto.setTanggalLahir(biodata.getTanggalLahir());
        dto.setJenisKelamin(biodata.getJenisKelamin()); // <-- DITAMBAHKAN
        dto.setEmailPribadi(biodata.getEmailPribadi());
        dto.setNomorTelepon(biodata.getNomorTelepon());
        dto.setAlamat(biodata.getAlamat());
        dto.setKontakDarurat(biodata.getKontakDarurat());
        return dto;
    }

    /**
     * BARU: Menghitung ringkasan akademik (IPK dan Total SKS) untuk mahasiswa.
     */
    /**
     * BARU: Menghitung ringkasan akademik (IPK, Total SKS, dan Semester Aktif) untuk mahasiswa.
     */
    public AkademikSummaryDTO getAkademikSummary(String username) {
        Mahasiswa mahasiswa = getMahasiswaFromUsername(username);

        List<Krs> krsList = krsRepository.findByMahasiswa(mahasiswa).stream()
                .filter(krs -> krs.getNilaiHuruf() != null && !krs.getNilaiHuruf().isBlank())
                .collect(Collectors.toList());

        int totalSks = 0;
        BigDecimal totalBobotSks = BigDecimal.ZERO;
        Set<String> periodeSelesai = new HashSet<>(); // Untuk menghitung semester unik

        for (Krs krs : krsList) {
            int sks = krs.getKelas().getMataKuliah().getSks();
            BigDecimal bobot = getBobotNilai(krs.getNilaiHuruf());

            totalSks += sks;
            totalBobotSks = totalBobotSks.add(bobot.multiply(new BigDecimal(sks)));

            // Tambahkan kombinasi tahun akademik & semester ke Set
            String periode = krs.getKelas().getTahunAkademik() + "-" + krs.getKelas().getSemester().name();
            periodeSelesai.add(periode);
        }

        BigDecimal ipk = BigDecimal.ZERO;
        if (totalSks > 0) {
            ipk = totalBobotSks.divide(new BigDecimal(totalSks), 2, RoundingMode.HALF_UP);
        }

        // Semester saat ini = jumlah periode yang selesai + 1
        int semesterAktif = periodeSelesai.size() + 1;

        AkademikSummaryDTO summaryDTO = new AkademikSummaryDTO();
        summaryDTO.setTotalSks(totalSks);
        summaryDTO.setIpk(ipk);
        summaryDTO.setSemesterAktif(semesterAktif); // Set nilai semester aktif

        return summaryDTO;
    }

    /**
     * BARU: Helper untuk mendapatkan bobot dari nilai huruf.
     */
    private BigDecimal getBobotNilai(String nilaiHuruf) {
        switch (nilaiHuruf.toUpperCase()) {
            case "A": return new BigDecimal("4.0");
            case "A-": return new BigDecimal("3.7");
            case "B+": return new BigDecimal("3.3");
            case "B": return new BigDecimal("3.0");
            case "B-": return new BigDecimal("2.7");
            case "C+": return new BigDecimal("2.3");
            case "C": return new BigDecimal("2.0");
            case "D": return new BigDecimal("1.0");
            case "E": return new BigDecimal("0.0");
            default: return BigDecimal.ZERO;
        }
    }

    /**
     * HELPER BARU: Konversi entitas Kelas ke DTO.
     */
    private KelasDTO convertToKelasDTO(Kelas kelas) {
        KelasDTO dto = new KelasDTO();
        dto.setKelasId(kelas.getKelasId());
        dto.setKodeMataKuliah(kelas.getMataKuliah().getKodeMatkul());
        dto.setNamaMataKuliah(kelas.getMataKuliah().getNamaMatkul());
        dto.setSks(kelas.getMataKuliah().getSks());
        dto.setDosenPengajar(kelas.getDosen().getNamaLengkap());
        // Menggabungkan hari dan jam menjadi satu string jadwal
        dto.setJadwal(kelas.getHari() + ", " + kelas.getJamMulai());
        dto.setRuangan(kelas.getRuangan());
        return dto;
    }
}