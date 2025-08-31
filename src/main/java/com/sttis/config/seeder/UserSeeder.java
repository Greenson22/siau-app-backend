package com.sttis.config.seeder;

import com.sttis.models.entities.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserSeeder {

    private final AdminSeeder adminSeeder;

    public UserSeeder(AdminSeeder adminSeeder) {
        this.adminSeeder = adminSeeder;
    }

    public void seed() {
        adminSeeder.seed();
        System.out.println("Seeder: Data Admin berhasil dibuat.");
    }
    
    public User getAdminUser() {
        return adminSeeder.getAdminUser();
    }
}