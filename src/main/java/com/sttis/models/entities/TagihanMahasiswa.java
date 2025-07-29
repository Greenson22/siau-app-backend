package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.sttis.models.entities.enums.StatusTagihan;

@Data
@Entity
@Table(name = "tagihan_mahasiswa")
public class TagihanMahasiswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagihanId;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", nullable = false)
    private Mahasiswa mahasiswa;

    @Column(nullable = false)
    private String deskripsiTagihan;

    @Column(nullable = false)
    private BigDecimal totalTagihan;

    @Column(nullable = false)
    private LocalDate tanggalJatuhTempo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTagihan status;
    
    @OneToMany(mappedBy="tagihanMahasiswa")
    private List<DetailTagihan> detailTagihanList;
    
    @OneToMany(mappedBy="tagihan")
    private List<Pembayaran> pembayaranList;
}