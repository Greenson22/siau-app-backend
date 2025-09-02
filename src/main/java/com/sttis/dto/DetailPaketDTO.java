package com.sttis.dto;

import lombok.Data;

@Data
public class DetailPaketDTO {
    private Integer matkulId;
    private String kodeMatkul;
    private String namaMatkul;
    private Integer sks; // <-- TAMBAHKAN BARIS INI
}