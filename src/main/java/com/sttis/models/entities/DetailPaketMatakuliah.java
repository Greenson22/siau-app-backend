package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detail_paket_matakuliah")
public class DetailPaketMatakuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailPaketId;

    @ManyToOne
    @JoinColumn(name = "paket_id", nullable = false)
    private PaketMatakuliah paketMatakuliah;

    @ManyToOne
    @JoinColumn(name = "matkul_id", nullable = false)
    private MataKuliah mataKuliah;
}