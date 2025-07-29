package com.sttis.models.repos;

import com.sttis.models.entities.DetailPaketMatakuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPaketMatakuliahRepository extends JpaRepository<DetailPaketMatakuliah, Integer> {
}