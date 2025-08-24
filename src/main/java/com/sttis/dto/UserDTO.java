package com.sttis.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private String roleName;
    private String namaLengkap; // <-- FIELD BARU DITAMBAHKAN
}