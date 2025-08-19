// program/java-spring-boot/com/sttis/dto/TrenPendaftaranDTO.java
package com.sttis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrenPendaftaranDTO {
    private String periode; // Contoh: "2025-01"
    private Long jumlah;
}