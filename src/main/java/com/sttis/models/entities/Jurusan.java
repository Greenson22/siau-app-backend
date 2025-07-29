package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "jurusan")
public class Jurusan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jurusanId;

    @Column(nullable = false)
    private String namaJurusan;

    @Column(nullable = false)
    private String fakultas;
    
    // Relasi ke entitas lain
    @OneToMany(mappedBy = "jurusan")
    private List<Mahasiswa> mahasiswas;

    @OneToMany(mappedBy = "jurusan")
    private List<Dosen> dosens;
    
    @OneToMany(mappedBy = "jurusan")
    private List<MataKuliah> mataKuliahs;

    @OneToMany(mappedBy = "jurusan")
    private List<PaketMatakuliah> paketMatakuliahs;
    
    @OneToMany(mappedBy = "jurusan")
    private List<Kurikulum> kurikulums;
}