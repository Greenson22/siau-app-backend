package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.sttis.models.entities.enums.StatusMataKuliah;

@Data
@Entity
@Table(name = "detail_kurikulum")
public class DetailKurikulum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailKurikulumId;

    @ManyToOne
    @JoinColumn(name = "kurikulum_id", nullable = false)
    private Kurikulum kurikulum;

    @ManyToOne
    @JoinColumn(name = "matkul_id", nullable = false)
    private MataKuliah mataKuliah;

    @Column(nullable = false)
    private Integer semesterTarget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMataKuliah statusMatkul;
}