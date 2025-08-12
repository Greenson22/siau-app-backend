// main/java/com/sttis/models/entities/BiodataMahasiswa.java
package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "biodata_mahasiswa")
public class BiodataMahasiswa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer biodataMahasiswaId;

    @OneToOne
    @JoinColumn(name = "mahasiswa_id", referencedColumnName = "mahasiswaId", nullable = false, unique = true)
    private Mahasiswa mahasiswa;

    @Lob
    private String alamat;

    private String nomorTelepon;
    private String emailPribadi;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String jenisKelamin; // <-- DITAMBAHKAN
    private String kontakDarurat;
}