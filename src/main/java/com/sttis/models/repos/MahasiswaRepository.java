// program/java-spring-boot/com/sttis/models/repos/MahasiswaRepository.java
package com.sttis.models.repos;

import com.sttis.dto.JurusanPendaftarDTO;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.entities.enums.StatusMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Integer>, JpaSpecificationExecutor<Mahasiswa> {
    
    Optional<Mahasiswa> findByNim(String nim);

    long countByStatus(StatusMahasiswa status); 

    // METHOD UNTUK MENGAMBIL DATA PENDAFTARAN
    @Query("SELECT new com.sttis.dto.JurusanPendaftarDTO(j.namaJurusan, COUNT(m)) FROM Mahasiswa m JOIN m.jurusan j GROUP BY j.namaJurusan")
    List<JurusanPendaftarDTO> findSebaranJurusanPendaftar();
}