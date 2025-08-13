package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AkademikSummaryDTO {
    private BigDecimal ipk;
    private Integer totalSks;
    private Integer semesterAktif; // <-- FIELD BARU
}