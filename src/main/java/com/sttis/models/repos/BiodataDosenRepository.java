package com.sttis.models.repos;

import com.sttis.models.entities.BiodataDosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiodataDosenRepository extends JpaRepository<BiodataDosen, Integer> {
}