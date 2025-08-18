// program/java-spring-boot/com/sttis/models/repos/DosenRepository.java
package com.sttis.models.repos;

import com.sttis.models.entities.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Integer>, JpaSpecificationExecutor<Dosen> { // <-- TAMBAHKAN INI
    Optional<Dosen> findByNidn(String nidn);
}