package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "dosen")
public class Dosen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dosenId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String nidn;

    @Column(nullable = false)
    private String namaLengkap;

    @ManyToOne
    @JoinColumn(name = "jurusan_id", nullable = false)
    private Jurusan jurusan;
    
    // Relasi ke entitas lain
    @OneToMany(mappedBy = "pembimbingAkademik")
    private List<Mahasiswa> mahasiswaBimbingan;

    @OneToMany(mappedBy = "dosen")
    private List<Kelas> kelasMengajar;
    
    @OneToOne(mappedBy = "dosen")
    private BiodataDosen biodata;
}