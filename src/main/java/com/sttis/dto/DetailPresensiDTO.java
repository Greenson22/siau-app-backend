package com.sttis.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DetailPresensiDTO {
    private Integer pertemuanKe;
    private LocalDate tanggal;
    private String statusHadir;
}