package com.sttis.models.repos;

import com.sttis.models.entities.Krs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrsRepository extends JpaRepository<Krs, Integer> {
}