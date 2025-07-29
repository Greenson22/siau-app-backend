package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "detail_tagihan")
public class DetailTagihan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "tagihan_id", nullable = false)
    private TagihanMahasiswa tagihanMahasiswa;

    @ManyToOne
    @JoinColumn(name = "komponen_id", nullable = false)
    private KomponenBiaya komponenBiaya;

    @Column(nullable = false)
    private BigDecimal jumlah;

    private String keterangan;
}