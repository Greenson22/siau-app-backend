package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "mata_kuliah")
public class MataKuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matkulId;

    @Column(nullable = false, unique = true)
    private String kodeMatkul;

    @Column(nullable = false)
    private String namaMatkul;

    @Column(nullable = false)
    private Integer sks;

    @ManyToOne
    @JoinColumn(name = "jurusan_id", nullable = false)
    private Jurusan jurusan;
    
    @OneToMany(mappedBy = "mataKuliah")
    private List<Kelas> kelasList;
}