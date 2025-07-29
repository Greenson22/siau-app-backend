package com.sttis.models.repos;

import com.sttis.models.entities.DetailKurikulum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailKurikulumRepository extends JpaRepository<DetailKurikulum, Integer> {
}
