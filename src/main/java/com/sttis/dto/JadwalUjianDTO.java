package com.sttis.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class JadwalUjianDTO {
    private String mataKuliah;
    private String jenisUjian;
    private LocalDate tanggalUjian;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String ruangan;
    private String dosenPengampu;
}