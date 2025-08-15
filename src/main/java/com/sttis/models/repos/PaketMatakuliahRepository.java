package com.sttis.models.repos;

import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.PaketMatakuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // <-- Tambahkan import ini

@Repository
public interface PaketMatakuliahRepository extends JpaRepository<PaketMatakuliah, Integer> {
    // Method baru untuk mencari paket berdasarkan jurusan dan semester
    Optional<PaketMatakuliah> findByJurusanAndSemesterKe(Jurusan jurusan, Integer semesterKe);
}
