package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal; // <-- Import BigDecimal

@Data
public class KrsDTO {
    private Integer krsId;
    private Integer kelasId;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private Integer sks;
    private String namaDosen;
    private String statusPersetujuan;
    private String jadwal; // Contoh: "Senin, 08:00"
    
    // --- PROPERTI BARU ---
    private BigDecimal nilaiAkhir;
    private String nilaiHuruf;
}