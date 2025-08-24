package com.sttis.config.seeder;

import com.sttis.models.entities.Role;
import com.sttis.models.repos.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean isSeeded() {
        return roleRepository.count() > 0;
    }

    public void seed() {
        Role adminRole = new Role(); adminRole.setRoleName("Admin");
        Role dosenRole = new Role(); dosenRole.setRoleName("Dosen");
        Role mahasiswaRole = new Role(); mahasiswaRole.setRoleName("Mahasiswa");
        
        roleRepository.save(adminRole);
        roleRepository.save(dosenRole);
        roleRepository.save(mahasiswaRole);

        System.out.println("Seeder: Data Role berhasil dibuat.");
    }
}