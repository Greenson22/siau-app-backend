package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaketMatakuliahDTO {
    private String namaPaket;
    private Integer totalSks;
    private List<DetailPaketDTO> detailPaket;
}