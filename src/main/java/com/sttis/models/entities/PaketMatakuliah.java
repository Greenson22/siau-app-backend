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
    private Integer semesterKe;

    @Column(nullable = false)
    private String tahunAkademik;

    @OneToMany(mappedBy = "paketMatakuliah")
    private List<DetailPaketMatakuliah> detailPaket;
}