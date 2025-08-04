package com.sttis.dto;

import lombok.Data;

@Data
public class VerifikasiPembayaranDTO {
    // Menerima "BERHASIL" atau "GAGAL"
    private String statusVerifikasi;
}