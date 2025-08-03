package com.sttis.models.repos;

import com.sttis.models.entities.Kelas;
import com.sttis.models.entities.Krs;
import com.sttis.models.entities.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KrsRepository extends JpaRepository<Krs, Integer> {

    /**
     * Mencari semua entri KRS berdasarkan objek Mahasiswa.
     * Spring Data JPA akan otomatis membuat query berdasarkan nama method ini.
     *
     * @param mahasiswa Objek mahasiswa yang KRS-nya ingin dicari.
     * @return Daftar KRS milik mahasiswa tersebut.
     */
    List<Krs> findByMahasiswa(Mahasiswa mahasiswa);

    /**
     * Memeriksa apakah sudah ada entri KRS untuk kombinasi Mahasiswa dan Kelas tertentu.
     * Berguna untuk mencegah mahasiswa mengambil mata kuliah yang sama lebih dari sekali.
     *
     * @param mahasiswa Objek mahasiswa.
     * @param kelas Objek kelas.
     * @return true jika entri sudah ada, false jika belum.
     */
    boolean existsByMahasiswaAndKelas(Mahasiswa mahasiswa, Kelas kelas);
}