package com.sttis.dto; // Buat package baru bernama 'dto'

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private String roleName; // Kita hanya tampilkan nama rolenya
}