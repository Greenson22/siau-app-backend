package com.sttis.models.repos;

import com.sttis.models.entities.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Integer> {
    // Contoh custom query: mencari dosen berdasarkan NIDN
    Optional<Dosen> findByNidn(String nidn);
}