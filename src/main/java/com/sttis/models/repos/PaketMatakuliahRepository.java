package com.sttis.models.repos;

import com.sttis.models.entities.PaketMatakuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaketMatakuliahRepository extends JpaRepository<PaketMatakuliah, Integer> {
}
