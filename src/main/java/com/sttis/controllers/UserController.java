package com.sttis.controllers;

import com.sttis.dto.ChangePasswordDTO;
import com.sttis.dto.UserDTO;
import com.sttis.dto.UserRegistrationDTO;
import com.sttis.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller untuk menangani semua operasi yang berkaitan dengan User.
 */
@RestController
@RequestMapping("/api/users") // Base path untuk semua endpoint di controller ini
public class UserController {

    private final UserService userService;

    // Melakukan dependency injection untuk UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint untuk melihat daftar semua pengguna.
     * Method: GET
     * URL: /api/users
     * @return Daftar pengguna dalam format DTO dan status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint untuk membuat pengguna baru (mahasiswa, dosen, dll).
     * Method: POST
     * URL: /api/users
     * @param registrationDTO Data pengguna baru dari request body.
     * @return Data pengguna yang baru dibuat dan status 201 Created.
     */
    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        // Logika pembuatan user didelegasikan sepenuhnya ke service layer
        UserDTO createdUser = userService.registerNewUser(registrationDTO); 
        // Catatan: Pastikan method registerNewUser di service Anda mengembalikan UserDTO
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

        /**
     * ENDPOINT BARU untuk mengubah password pengguna yang sedang login.
     * Method: POST
     * URL: /api/users/change-password
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(authentication.getName(), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password berhasil diperbarui."));
    }
}