package com.sttis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Username tidak boleh kosong")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;

    @NotNull(message = "Role ID tidak boleh kosong")
    private Integer roleId;

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    private String namaLengkap;

    // Spesifik untuk mahasiswa
    private String nim;

    // Spesifik untuk dosen
    private String nidn;

    @NotNull(message = "Jurusan ID tidak boleh kosong")
    private Integer jurusanId;

    // Tambahkan field lain jika perlu, misal: dosen_pa_id untuk mahasiswa
}