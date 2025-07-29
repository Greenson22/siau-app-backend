package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.sttis.models.entities.enums.StatusPersetujuan;

@Data
@Entity
@Table(name = "krs")
public class Krs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer krsId;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", nullable = false)
    private Mahasiswa mahasiswa;

    @ManyToOne
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

    @Enumerated(EnumType.STRING)
    private StatusPersetujuan statusPersetujuan;

    private BigDecimal nilaiAkhir;
    private String nilaiHuruf;

    @OneToMany(mappedBy = "krs")
    private List<PresensiMahasiswa> presensiList;
}