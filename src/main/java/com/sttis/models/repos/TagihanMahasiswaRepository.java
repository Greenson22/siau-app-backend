package com.sttis.models.repos;

import com.sttis.models.entities.TagihanMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagihanMahasiswaRepository extends JpaRepository<TagihanMahasiswa, Integer> {
}
