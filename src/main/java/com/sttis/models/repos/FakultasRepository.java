// java-spring-boot/com/sttis/models/repos/FakultasRepository.java
package com.sttis.models.repos;

import com.sttis.models.entities.Fakultas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FakultasRepository extends JpaRepository<Fakultas, Integer> {
    Optional<Fakultas> findByNamaFakultas(String namaFakultas);
}