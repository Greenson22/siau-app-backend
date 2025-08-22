// program/java-spring-boot/com/sttis/dto/IpsDataDTO.java
package com.sttis.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class IpsDataDTO {
    private List<String> labels; // Contoh: ["Sm 1", "Sm 2"]
    private List<BigDecimal> data;   // Contoh: [3.50, 3.75]
}