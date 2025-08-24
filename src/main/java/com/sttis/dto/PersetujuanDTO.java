package com.sttis.dto;

import lombok.Data;

@Data
public class PersetujuanDTO {
    // Menerima "DISETUJUI" atau "DITOLAK"
    private String statusPersetujuan;
    private String catatanPenolakan; // <-- FIELD BARU
}