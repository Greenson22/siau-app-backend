package com.sttis.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaketMatakuliahDTO {
    private Integer paketId; // DITAMBAHKAN
    private String namaPaket;
    private String namaJurusan; // DITAMBAHKAN
    private Integer semester; // Tipe diubah dari String ke Integer
    private Integer totalSks;
    private List<DetailPaketDTO> detailPaket;
}
