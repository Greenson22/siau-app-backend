package com.sttis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MataKuliahDTO {
    private Integer matkulId;

    @NotBlank(message = "Kode mata kuliah tidak boleh kosong")
    private String kodeMatkul;

    @NotBlank(message = "Nama mata kuliah tidak boleh kosong")
    private String namaMatkul;

    @NotNull(message = "SKS tidak boleh kosong")
    private Integer sks;

    @NotNull(message = "ID Jurusan tidak boleh kosong")
    private Integer jurusanId;
    
    // Opsional, untuk menampilkan nama jurusan saat GET
    private String namaJurusan; 
}