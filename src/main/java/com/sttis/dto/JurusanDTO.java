// java-spring-boot/com/sttis/dto/JurusanDTO.java
package com.sttis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JurusanDTO {
    private Integer jurusanId;

    @NotBlank(message = "Nama jurusan tidak boleh kosong.")
    private String namaJurusan;

    // --- PERUBAHAN ---
    @NotNull(message = "ID Fakultas tidak boleh kosong.")
    private Integer fakultasId;
    
    // Opsional, untuk menampilkan nama saat GET
    private String namaFakultas;
}