package com.sttis.config.seeder;

import com.sttis.models.entities.Jurusan;
import com.sttis.models.repos.JurusanRepository;
import org.springframework.stereotype.Component;

@Component
public class JurusanSeeder {

    private final JurusanRepository jurusanRepository;

    public JurusanSeeder(JurusanRepository jurusanRepository) {
        this.jurusanRepository = jurusanRepository;
    }

    public void seed() {
        Jurusan teologi = new Jurusan(); 
        teologi.setNamaJurusan("S1 Teologi"); 
        teologi.setFakultas("Fakultas Teologi"); 
        jurusanRepository.save(teologi);
        
        Jurusan pak = new Jurusan(); 
        pak.setNamaJurusan("S1 Pendidikan Agama Kristen"); 
        pak.setFakultas("Fakultas Pendidikan"); 
        jurusanRepository.save(pak);

        System.out.println("Seeder: Data Jurusan berhasil dibuat.");
    }
}