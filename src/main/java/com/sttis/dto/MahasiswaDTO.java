package com.sttis.dto;

import lombok.Data;

@Data
public class MahasiswaDTO {
    private Integer mahasiswaId;
    private String nim;
    private String namaLengkap;
    private String status;
    private String namaJurusan;
    private String namaDosenPA; // Nama Dosen Pembimbing Akademik
}