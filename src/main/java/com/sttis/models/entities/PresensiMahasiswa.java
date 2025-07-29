package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import com.sttis.models.entities.enums.StatusHadir;

@Data
@Entity
@Table(name = "presensi_mahasiswa")
public class PresensiMahasiswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer presensiId;

    @ManyToOne
    @JoinColumn(name = "krs_id", nullable = false)
    private Krs krs;

    @Column(nullable = false)
    private Integer pertemuanKe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusHadir statusHadir;

    @Column(nullable = false)
    private LocalDate tanggal;
}