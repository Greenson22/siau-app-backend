// main/java/com/sttis/dto/BiodataUpdateDTO.java
package com.sttis.dto;

import lombok.Data;

@Data
public class BiodataUpdateDTO {
    private String alamat;
    private String nomorTelepon;
    private String emailPribadi;
    private String kontakDarurat;
    private String jenisKelamin; // <-- DITAMBAHKAN
}