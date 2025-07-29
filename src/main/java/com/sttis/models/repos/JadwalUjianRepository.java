package com.sttis.models.repos;

import com.sttis.models.entities.JadwalUjian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JadwalUjianRepository extends JpaRepository<JadwalUjian, Integer> {
}