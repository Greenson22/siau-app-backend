package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.sttis.models.entities.enums.JenisBiaya;;

@Data
@Entity
@Table(name = "komponen_biaya")
public class KomponenBiaya {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer komponenId;

    @Column(nullable = false)
    private String namaKomponen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisBiaya jenisBiaya;
}