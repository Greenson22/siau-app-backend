package com.sttis.models.repos;

import com.sttis.models.entities.Pembayaran;
import com.sttis.models.entities.enums.StatusVerifikasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; 

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Integer> {
    long countByStatusVerifikasi(StatusVerifikasi status);
    
    List<Pembayaran> findByStatusVerifikasi(StatusVerifikasi status);
}