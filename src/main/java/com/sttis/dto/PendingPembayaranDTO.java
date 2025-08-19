package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PendingPembayaranDTO {
    private Integer pembayaranId;
    private String namaMahasiswa;
    private String deskripsiTagihan;
    private BigDecimal jumlahBayar;
    private LocalDateTime tanggalBayar;
}