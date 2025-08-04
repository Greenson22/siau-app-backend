package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class KhsDTO {
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private Integer sks;
    private String tahunAkademik;
    private String semester;
    private BigDecimal nilaiAkhir;
    private String nilaiHuruf;
}