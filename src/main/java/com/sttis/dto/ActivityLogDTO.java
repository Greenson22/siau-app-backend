package com.sttis.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityLogDTO {
    private Long logId;
    private String username;
    private String aksi;
    private String deskripsi;
    private String ipAddress;
    private LocalDateTime timestamp;
}