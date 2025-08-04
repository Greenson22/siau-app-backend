package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class RekapPresensiDTO {
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String namaDosen;
    private List<DetailPresensiDTO> detailPresensi;
}