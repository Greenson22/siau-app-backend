package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private LocalDateTime lastLogin;
    
    // Relasi ke entitas lain
    @OneToOne(mappedBy = "user")
    private Mahasiswa mahasiswa;

    @OneToOne(mappedBy = "user")
    private Dosen dosen;

    @OneToMany(mappedBy = "pembuat")
    private List<Pengumuman> pengumuman;

    @OneToMany(mappedBy = "user")
    private List<ActivityLog> activityLogs;
}