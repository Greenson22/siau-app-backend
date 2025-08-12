// main/java/com/sttis/dto/BiodataMahasiswaDTO.java
package com.sttis.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BiodataMahasiswaDTO {
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String jenisKelamin; // <-- DITAMBAHKAN
    private String emailPribadi;
    private String nomorTelepon;
    private String alamat;
    private String kontakDarurat;
}