package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String aksi;
    
    @Lob
    private String deskripsi;
    
    private String ipAddress;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
}