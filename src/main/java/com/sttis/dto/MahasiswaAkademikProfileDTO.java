// program/java-spring-boot/com/sttis/dto/MahasiswaAkademikProfileDTO.java
package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class MahasiswaAkademikProfileDTO {
    private AkademikSummaryDTO summary;
    private List<KhsDTO> khsData;
    private IpsDataDTO ipsHistory;
}