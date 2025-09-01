// java-spring-boot/com/sttis/dto/FakultasDTO.java
package com.sttis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FakultasDTO {
    private Integer fakultasId;

    @NotBlank(message = "Nama fakultas tidak boleh kosong")
    private String namaFakultas;
}