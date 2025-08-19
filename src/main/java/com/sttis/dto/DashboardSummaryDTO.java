package com.sttis.dto;

import lombok.Data;

@Data
public class DashboardSummaryDTO {
    private long pendaftarBaru;
    private long totalMahasiswaAktif;
    private long totalDosen;
    private long pembayaranMenungguVerifikasi;
}