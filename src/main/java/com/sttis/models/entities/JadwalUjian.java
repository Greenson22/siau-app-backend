package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import com.sttis.models.entities.enums.JenisUjian;

@Data
@Entity
@Table(name = "jadwal_ujian")
public class JadwalUjian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ujianId;

    @ManyToOne
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisUjian jenisUjian;

    @Column(nullable = false)
    private LocalDate tanggalUjian;

    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String ruangan;
}