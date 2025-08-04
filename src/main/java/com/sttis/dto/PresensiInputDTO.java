package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class PresensiInputDTO {
    private Integer pertemuanKe;
    private List<StatusPresensiDTO> statusPresensi;

    @Data
    public static class StatusPresensiDTO {
        private Integer mahasiswaId;
        private String statusHadir; // Contoh: "HADIR", "IZIN", "SAKIT", "ALFA"
    }
}