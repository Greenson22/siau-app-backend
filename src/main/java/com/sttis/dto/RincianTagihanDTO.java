package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RincianTagihanDTO {
    private String namaKomponen;
    private BigDecimal jumlah;
    private String keterangan;
}