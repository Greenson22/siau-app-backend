package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TagihanInputDTO {
    private Integer mahasiswaId;
    private String deskripsiTagihan;
    private LocalDate tanggalJatuhTempo;
    private List<RincianInputDTO> rincian;

    @Data
    public static class RincianInputDTO {
        private Integer komponenId;
        private BigDecimal jumlah;
        private String keterangan;
    }
}