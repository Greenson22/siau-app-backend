// program/java-spring-boot/com/sttis/dto/JurusanPendaftarDTO.java
package com.sttis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JurusanPendaftarDTO {
    private String namaJurusan;
    private Long jumlah;
}