package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "kurikulum")
public class Kurikulum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kurikulumId;

    @ManyToOne
    @JoinColumn(name = "jurusan_id", nullable = false)
    private Jurusan jurusan;

    @Column(nullable = false)
    private String namaKurikulum;

    @Column(nullable = false)
    private String tahunBerlaku;
    
    @OneToMany(mappedBy="kurikulum")
    private List<DetailKurikulum> detailKurikulumList;
}