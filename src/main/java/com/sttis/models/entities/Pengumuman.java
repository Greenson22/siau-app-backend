package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pengumuman")
public class Pengumuman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pengumumanId;

    @Column(nullable = false)
    private String judul;

    @Lob // Untuk tipe data TEXT
    @Column(nullable = false)
    private String isi;

    @ManyToOne
    @JoinColumn(name = "user_id_pembuat", nullable = false)
    private User pembuat;

    @Column(nullable = false)
    private LocalDateTime tanggalTerbit;
}