package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class ActionItemDTO {
    private List<PendingPembayaranDTO> pembayaranMenungguVerifikasi;
    // Di masa depan, bisa ditambahkan list lain seperti:
    // private List<PendaftarBaruDTO> pendaftarBelumDivalidasi;
    // private List<NotifikasiSistemDTO> notifikasiSistem;
}