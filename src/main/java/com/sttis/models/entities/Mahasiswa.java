package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.sttis.models.entities.enums.StatusMahasiswa;

@Data
@Entity
@Table(name = "mahasiswa")
public class Mahasiswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mahasiswaId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String nim;

    @Column(nullable = false)
    private String namaLengkap;

    @ManyToOne
    @JoinColumn(name = "jurusan_id", nullable = false)
    private Jurusan jurusan;

    @ManyToOne
    @JoinColumn(name = "dosen_pa_id")
    private Dosen pembimbingAkademik;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMahasiswa status;
    
    // Relasi ke entitas lain
    @OneToMany(mappedBy = "mahasiswa")
    private List<Krs> krsList;
    
    @OneToOne(mappedBy = "mahasiswa")
    private BiodataMahasiswa biodata;
    
    @OneToMany(mappedBy = "mahasiswa")
    private List<TagihanMahasiswa> tagihanList;
}