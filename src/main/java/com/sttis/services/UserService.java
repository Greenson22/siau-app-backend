package com.sttis.services;

import com.sttis.dto.UserDTO;
import com.sttis.models.entities.User;
import com.sttis.models.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method untuk mengambil semua user dan mengubahnya ke DTO
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method untuk konversi User -> UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        // Mengambil nama role dari relasi, hindari null pointer
        if (user.getRole() != null) {
            userDTO.setRoleName(user.getRole().getRoleName());
        }
        return userDTO;
    }
}
