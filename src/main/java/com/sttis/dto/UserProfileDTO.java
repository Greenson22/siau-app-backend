package com.sttis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO {
    private Integer userId;
    private String username;
    private String role;
    private MahasiswaDTO mahasiswaInfo;
    private DosenDTO dosenInfo;
}