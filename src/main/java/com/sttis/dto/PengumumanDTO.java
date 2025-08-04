package com.sttis.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PengumumanDTO {
    private Integer pengumumanId;
    private String judul;
    private String isi;
    private String namaPembuat;
    private LocalDateTime tanggalTerbit;
}