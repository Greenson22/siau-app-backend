package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.sttis.models.entities.enums.StatusVerifikasi;

@Data
@Entity
@Table(name = "pembayaran")
public class Pembayaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pembayaranId;

    @ManyToOne
    @JoinColumn(name = "tagihan_id", nullable = false)
    private TagihanMahasiswa tagihan;

    @Column(nullable = false)
    private LocalDateTime tanggalBayar;

    @Column(nullable = false)
    private BigDecimal jumlahBayar;

    private String metodePembayaran;
    private String buktiBayar;

    @Enumerated(EnumType.STRING)
    private StatusVerifikasi statusVerifikasi;
}