package com.sttis.models.repos;

import com.sttis.models.entities.Pembayaran;
import com.sttis.models.entities.enums.StatusVerifikasi; // <-- IMPORT BARU
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Integer> {
    // METHOD BARU UNTUK MENGHITUNG PEMBAYARAN PENDING
    long countByStatusVerifikasi(StatusVerifikasi status);
}