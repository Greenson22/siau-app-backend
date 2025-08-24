package com.sttis.config.seeder;

import com.sttis.models.entities.Role;
import com.sttis.models.entities.User;
import com.sttis.models.repos.RoleRepository;
import com.sttis.models.repos.UserRepository;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private User adminUser;

    public AdminSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        Role adminRole = roleRepository.findAll().stream()
                .filter(r -> r.getRoleName().equals("Admin")).findFirst().orElseThrow();
        
        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRole(adminRole);
        userRepository.save(adminUser);
        
        System.out.println("Seeder: Data Admin berhasil dibuat.");
    }
}