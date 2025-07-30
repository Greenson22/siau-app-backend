package com.sttis.services;

import com.sttis.dto.UserDTO;
import com.sttis.dto.UserRegistrationDTO;
import com.sttis.models.entities.*;
import com.sttis.models.entities.enums.StatusMahasiswa;
import com.sttis.models.repos.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    // --- DEPENDENSI ---
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;
    private final JurusanRepository jurusanRepository;
    private final PasswordEncoder passwordEncoder;

    // --- CONSTRUCTOR INJECTION ---
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       MahasiswaRepository mahasiswaRepository, DosenRepository dosenRepository,
                       JurusanRepository jurusanRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
        this.jurusanRepository = jurusanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Mengambil semua data pengguna dan mengonversinya menjadi DTO.
     * @return Daftar UserDTO.
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mendaftarkan pengguna baru (Mahasiswa atau Dosen) ke dalam sistem.
     * @param registrationDTO Data registrasi dari klien.
     * @return Entitas User yang baru dibuat.
     */
    @Transactional // Menjamin semua operasi database (save user, mahasiswa, etc.) berhasil atau semua digagalkan.
    public UserDTO registerNewUser(UserRegistrationDTO registrationDTO) {
        // 1. Validasi apakah username sudah ada
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new IllegalStateException("Error: Username '" + registrationDTO.getUsername() + "' sudah digunakan.");
        }

        // 2. Cari Role dan Jurusan dari database
        Role role = roleRepository.findById(registrationDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Error: Role dengan ID " + registrationDTO.getRoleId() + " tidak ditemukan."));

        Jurusan jurusan = jurusanRepository.findById(registrationDTO.getJurusanId())
                .orElseThrow(() -> new RuntimeException("Error: Jurusan dengan ID " + registrationDTO.getJurusanId() + " tidak ditemukan."));

        // 3. Buat dan simpan entitas User baru
        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // Enkripsi password
        newUser.setRole(role);
        User savedUser = userRepository.save(newUser);

        // 4. Buat entitas Mahasiswa atau Dosen berdasarkan role
        if ("Mahasiswa".equalsIgnoreCase(role.getRoleName())) {
            if (registrationDTO.getNim() == null || registrationDTO.getNim().isBlank()) {
                throw new IllegalArgumentException("Error: NIM tidak boleh kosong untuk role Mahasiswa.");
            }
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setUser(savedUser);
            mahasiswa.setNim(registrationDTO.getNim());
            mahasiswa.setNamaLengkap(registrationDTO.getNamaLengkap());
            mahasiswa.setJurusan(jurusan);
            mahasiswa.setStatus(StatusMahasiswa.AKTIF);
            mahasiswaRepository.save(mahasiswa);

        } else if ("Dosen".equalsIgnoreCase(role.getRoleName())) {
            if (registrationDTO.getNidn() == null || registrationDTO.getNidn().isBlank()) {
                throw new IllegalArgumentException("Error: NIDN tidak boleh kosong untuk role Dosen.");
            }
            Dosen dosen = new Dosen();
            dosen.setUser(savedUser);
            dosen.setNidn(registrationDTO.getNidn());
            dosen.setNamaLengkap(registrationDTO.getNamaLengkap());
            dosen.setJurusan(jurusan);
            dosenRepository.save(dosen);
        }

        return convertToDTO(savedUser);
    }

    /**
     * Helper method privat untuk mengubah entitas User menjadi UserDTO.
     * @param user Entitas User dari database.
     * @return Objek UserDTO.
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        if (user.getRole() != null) {
            userDTO.setRoleName(user.getRole().getRoleName());
        }
        return userDTO;
    }
}