package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PembayaranDTO {
    private Integer pembayaranId;
    private Integer tagihanId;
    private String namaMahasiswa;
    private String deskripsiTagihan;
    private LocalDateTime tanggalBayar;
    private BigDecimal jumlahBayar;
    private String metodePembayaran;
    private String statusVerifikasi;
}