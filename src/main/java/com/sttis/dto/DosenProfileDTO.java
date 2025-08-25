package com.sttis.dto;

import lombok.Data;

@Data
public class DosenProfileDTO {
    // Dari entitas Dosen
    private String namaLengkap;
    private String nidn;
    private String namaJurusan;

    // Dari entitas BiodataDosen
    private String alamat;
    private String nomorTelepon;
    private String emailPribadi;
    private String spesialisasi;
    
    // Informasi tambahan yang bisa diperluas
    private String jabatanAkademik = "Dosen Tetap"; // Contoh data tambahan
}