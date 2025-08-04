package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PembayaranInputDTO {
    private Integer tagihanId;
    private BigDecimal jumlahBayar;
    private String metodePembayaran; // Contoh: "Transfer Bank"
    private String buktiBayar; // Bisa berupa URL atau path ke file
}