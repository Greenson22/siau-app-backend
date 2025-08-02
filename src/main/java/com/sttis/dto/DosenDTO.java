package com.sttis.dto;

import lombok.Data;

@Data
public class DosenDTO {
    private Integer dosenId;
    private String nidn;
    private String namaLengkap;
    private String namaJurusan;
    private String spesialisasi; // Diambil dari biodata
}