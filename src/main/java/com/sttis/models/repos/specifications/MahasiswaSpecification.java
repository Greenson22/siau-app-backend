package com.sttis.models.repos.specifications; // Buat package baru jika perlu

import com.sttis.models.entities.Jurusan;
import com.sttis.models.entities.Mahasiswa;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaSpecification {

    public static Specification<Mahasiswa> findByCriteria(String search, Integer jurusanId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter berdasarkan kata kunci pencarian (NIM atau Nama)
            if (StringUtils.hasText(search)) {
                String likePattern = "%" + search.toLowerCase() + "%";
                Predicate searchByNim = criteriaBuilder.like(criteriaBuilder.lower(root.get("nim")), likePattern);
                Predicate searchByNama = criteriaBuilder.like(criteriaBuilder.lower(root.get("namaLengkap")), likePattern);
                predicates.add(criteriaBuilder.or(searchByNim, searchByNama));
            }

            // 2. Filter berdasarkan ID Jurusan
            if (jurusanId != null) {
                Join<Mahasiswa, Jurusan> jurusanJoin = root.join("jurusan");
                predicates.add(criteriaBuilder.equal(jurusanJoin.get("jurusanId"), jurusanId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}