package com.sttis.models.repos;

import com.sttis.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Contoh custom query: mencari user berdasarkan username
    Optional<User> findByUsername(String username);
}
