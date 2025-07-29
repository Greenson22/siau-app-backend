package com.sttis.models.repos;

import com.sttis.models.entities.KomponenBiaya;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KomponenBiayaRepository extends JpaRepository<KomponenBiaya, Integer> {
}
