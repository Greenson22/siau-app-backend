// java-spring-boot/com/sttis/models/entities/Jurusan.java
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

    // --- PERUBAHAN ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fakultas_id", nullable = false)
    private Fakultas fakultas;
    
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