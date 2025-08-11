package com.sttis.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BiodataMahasiswaDTO {
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String emailPribadi;
    private String nomorTelepon;
    private String alamat;
    private String kontakDarurat;
    
    // Catatan: Email institusi dan Jenis Kelamin tidak ada di model data,
    // jadi tidak bisa disertakan dari backend.
}