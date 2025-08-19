// program/java-spring-boot/com/sttis/dto/ActionItemDTO.java
package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class ActionItemDTO {
    private List<PendingPembayaranDTO> pembayaranMenungguVerifikasi;
    private List<ActivityLogDTO> latestActivities;
}