package com.sttis.models.repos;

import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.enums.StatusMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- IMPORT INI
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Tambahkan JpaSpecificationExecutor<Mahasiswa>
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Integer>, JpaSpecificationExecutor<Mahasiswa> {
    
    Optional<Mahasiswa> findByNim(String nim);

    // METHOD BARU UNTUK MENGHITUNG MAHASISWA AKTIF
    long countByStatus(StatusMahasiswa status); 
}