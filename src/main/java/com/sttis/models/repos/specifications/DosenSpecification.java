package com.sttis.models.repos.specifications;

import com.sttis.models.entities.Dosen;
import com.sttis.models.entities.Jurusan;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DosenSpecification {

    public static Specification<Dosen> findByCriteria(String search, Integer jurusanId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String likePattern = "%" + search.toLowerCase() + "%";
                Predicate searchByNidn = criteriaBuilder.like(criteriaBuilder.lower(root.get("nidn")), likePattern);
                Predicate searchByNama = criteriaBuilder.like(criteriaBuilder.lower(root.get("namaLengkap")), likePattern);
                predicates.add(criteriaBuilder.or(searchByNidn, searchByNama));
            }

            if (jurusanId != null) {
                Join<Dosen, Jurusan> jurusanJoin = root.join("jurusan");
                predicates.add(criteriaBuilder.equal(jurusanJoin.get("jurusanId"), jurusanId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}