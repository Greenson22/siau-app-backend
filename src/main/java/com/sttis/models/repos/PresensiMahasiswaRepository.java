package com.sttis.models.repos;

import com.sttis.models.entities.Krs; // <-- Import Krs
import com.sttis.models.entities.PresensiMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresensiMahasiswaRepository extends JpaRepository<PresensiMahasiswa, Integer> {

    /**
     * Memeriksa apakah presensi untuk KRS dan pertemuan tertentu sudah ada.
     */
    boolean existsByKrsAndPertemuanKe(Krs krs, Integer pertemuanKe); // <-- TAMBAHKAN METHOD INI
}