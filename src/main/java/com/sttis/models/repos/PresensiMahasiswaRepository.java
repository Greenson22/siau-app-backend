package com.sttis.models.repos;

import com.sttis.models.entities.PresensiMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresensiMahasiswaRepository extends JpaRepository<PresensiMahasiswa, Integer> {
}