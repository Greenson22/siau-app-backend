package com.sttis.models.repos;

import com.sttis.models.entities.Mahasiswa; // <-- Import Mahasiswa
import com.sttis.models.entities.TagihanMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // <-- Import List

@Repository
public interface TagihanMahasiswaRepository extends JpaRepository<TagihanMahasiswa, Integer> {
    
    // TAMBAHKAN METHOD INI
    List<TagihanMahasiswa> findByMahasiswa(Mahasiswa mahasiswa);
}