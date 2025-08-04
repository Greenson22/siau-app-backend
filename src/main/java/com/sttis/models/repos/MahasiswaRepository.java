package com.sttis.models.repos;

import com.sttis.models.entities.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- IMPORT INI
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Tambahkan JpaSpecificationExecutor<Mahasiswa>
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Integer>, JpaSpecificationExecutor<Mahasiswa> {
    
    Optional<Mahasiswa> findByNim(String nim);
}