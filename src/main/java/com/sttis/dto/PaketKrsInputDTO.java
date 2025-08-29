package com.sttis.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class PaketKrsInputDTO {

    @NotBlank(message = "Nama paket tidak boleh kosong")
    private String namaPaket;

    @NotNull(message = "Jurusan ID tidak boleh kosong")
    private Integer jurusanId;

    @NotNull(message = "Semester tidak boleh kosong")
    private Integer semester;

    @NotNull(message = "Daftar mata kuliah tidak boleh kosong")
    private List<Integer> mataKuliahIds;
}
