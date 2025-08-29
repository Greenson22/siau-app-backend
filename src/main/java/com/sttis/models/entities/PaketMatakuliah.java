package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "paket_matakuliah")
public class PaketMatakuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paketId;

    @Column(nullable = false)
    private String namaPaket;

    @ManyToOne
    @JoinColumn(name = "jurusan_id", nullable = false)
    private Jurusan jurusan;

    @Column(nullable = false)
    private Integer semester; // Mengganti nama dari semesterKe

    // Tahun akademik tidak lagi relevan per paket, tapi per kelas
    // @Column(nullable = false)
    // private String tahunAkademik;

    @OneToMany(mappedBy = "paketMatakuliah", cascade = CascadeType.ALL, orphanRemoval = true) // Cascade & Orphan Removal
    private List<DetailPaketMatakuliah> detailPaket;
}
