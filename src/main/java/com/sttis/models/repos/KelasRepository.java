package com.sttis.models.repos;

import com.sttis.models.entities.Dosen; // <-- Import Dosen
import com.sttis.models.entities.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // <-- Import List

@Repository
public interface KelasRepository extends JpaRepository<Kelas, Integer> {

    /**
     * Mencari semua kelas yang diampu oleh dosen tertentu.
     * @param dosen Objek dosen yang mengajar.
     * @return Daftar kelas yang diampu oleh dosen tersebut.
     */
    List<Kelas> findByDosen(Dosen dosen); // <-- TAMBAHKAN METHOD INI
}