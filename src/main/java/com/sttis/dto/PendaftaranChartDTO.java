// program/java-spring-boot/com/sttis/dto/PendaftaranChartDTO.java
package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class PendaftaranChartDTO {
    private List<TrenPendaftaranDTO> trenPendaftaran;
    private List<JurusanPendaftarDTO> sebaranJurusan;
}