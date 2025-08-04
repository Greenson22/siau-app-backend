package com.sttis.dto;

import lombok.Data;

@Data
public class KelasDTO {
    private Integer kelasId;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private Integer sks;
    private String dosenPengajar;
    private String jadwal; // Contoh: "Senin, 08:00 - 10:30"
    private String ruangan;
}