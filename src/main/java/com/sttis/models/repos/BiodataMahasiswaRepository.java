package com.sttis.models.repos;

import com.sttis.models.entities.BiodataMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiodataMahasiswaRepository extends JpaRepository<BiodataMahasiswa, Integer> {
}