package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "biodata_dosen")
public class BiodataDosen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer biodataDosenId;

    @OneToOne
    @JoinColumn(name = "dosen_id", referencedColumnName = "dosenId", nullable = false, unique = true)
    private Dosen dosen;
    
    @Lob
    private String alamat;
    
    private String nomorTelepon;
    private String emailPribadi;
    private String spesialisasi;
}