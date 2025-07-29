package com.sttis.models.repos;

import com.sttis.models.entities.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Integer> {
    // Contoh custom query: mencari mahasiswa berdasarkan NIM
    Optional<Mahasiswa> findByNim(String nim);
}
