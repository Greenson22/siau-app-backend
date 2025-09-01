// java-spring-boot/com/sttis/models/entities/Fakultas.java
package com.sttis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "fakultas")
public class Fakultas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fakultasId;

    @Column(nullable = false, unique = true)
    private String namaFakultas;

    @OneToMany(mappedBy = "fakultas")
    private List<Jurusan> jurusans;
}