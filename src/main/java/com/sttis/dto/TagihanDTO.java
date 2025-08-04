package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TagihanDTO {
    private Integer tagihanId;
    private Integer mahasiswaId;
    private String namaMahasiswa;
    private String deskripsiTagihan;
    private BigDecimal totalTagihan;
    private LocalDate tanggalJatuhTempo;
    private String status;
    private List<RincianTagihanDTO> rincian;
}