package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;
import java.util.List;
import com.sttis.models.entities.enums.Semester;

@Data
@Entity
@Table(name = "kelas")
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kelasId;

    @ManyToOne
    @JoinColumn(name = "matkul_id", nullable = false)
    private MataKuliah mataKuliah;

    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @Column(nullable = false)
    private String tahunAkademik;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    private String hari;
    private LocalTime jamMulai;
    private String ruangan;

    @OneToMany(mappedBy = "kelas")
    private List<Krs> krsList;
    
    @OneToMany(mappedBy = "kelas")
    private List<JadwalUjian> jadwalUjianList;
}